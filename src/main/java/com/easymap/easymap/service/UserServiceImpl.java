package com.easymap.easymap.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.easymap.easymap.dto.request.review.ReviewUpdateRequestDTO;
import com.easymap.easymap.dto.request.user.UserNicknameDuplicateRequestDTO;
import com.easymap.easymap.dto.request.user.UserRequiredInfoRequestDto;
import com.easymap.easymap.dto.response.review.ReviewResponseDTO;
import com.easymap.easymap.entity.Review;
import com.easymap.easymap.entity.ReviewImg;
import com.easymap.easymap.entity.User;
import com.easymap.easymap.handler.exception.NickNameDuplicatedException;
import com.easymap.easymap.handler.exception.ResourceNotFoundException;
import com.easymap.easymap.repository.ReviewRepository;
import com.easymap.easymap.repository.UserRepository;
import com.easymap.easymap.service.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final S3Service s3Service;

    @Override
    public boolean userNicknameDuplicateCheck(UserNicknameDuplicateRequestDTO userNicknameDuplicateRequestDTO) {
        return isUserNicknameDuplicated(userNicknameDuplicateRequestDTO.getNickname());

    }

    /**
     * userID가 null인 경우 자기 정보 조회, 타인 정보인 경우 userId, profile_s3_key, nickname만 노출
     */
    @Override
    public User loadUserStatus(Long userId, UserDetails userDetails) {
        User user;
        log.info("userId:{}", userId);
        if(userId == null){
            user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                    () -> new ResourceNotFoundException("no active user :" + userDetails.getUsername())
            );
        }
        else{
            user = userRepository.findUserByUserIdAndDeactivationDateIsNull(userId).orElseThrow(
                    () -> new ResourceNotFoundException("no active user :" + userDetails.getUsername())
            );
            user.setEmail(null);
            user.setGender(null);
            user.setBirthdate(null);
            user.setOauthType(null);
            user.setSignupDate(null);
        }
        return user;
    }

    @Override
    public void userWithdraw(UserDetails userDetails) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findUserByEmailAndDeactivationDateIsNull(userDetails.getUsername());

        User user = foundUser.orElseThrow(() -> new ResourceNotFoundException("no active user :" + userDetails.getUsername()));

        user.setDeactivationDate(LocalDateTime.now());

        userRepository.save(user);

    }

    @Override
    public List<String> userAdditionalInfoCheck() {
        String userEmail = findUserEmailFromJwt();
        User user = userRepository.findUserByEmailAndDeactivationDateIsNull(userEmail).orElseThrow(
                () -> new ResourceNotFoundException("User not found with email: " + userEmail)
        );
        List<String> requireList = new ArrayList<>();
        if(user.getBirthdate() == null){
            requireList.add("birthdate");
        }
        if(user.getGender() == null){
            requireList.add("gender");
        }

        return requireList;
    }

    @Transactional
    @Override
    public boolean patchUserRequiredInfo(UserRequiredInfoRequestDto userInfo) {

        log.info("Received User Info: {}", userInfo);
        String userEmail = findUserEmailFromJwt();
        User user = userRepository.findUserByEmailAndDeactivationDateIsNull(userEmail).orElseThrow(
                () -> new ResourceNotFoundException("User not found with email: " + userEmail)
        );

        boolean result = false;

        if (userInfo.getGender() != null) {
            user.setGender(userInfo.getGender());
            result = true;
        }
        if (userInfo.getBirthdate() != null) {
            user.setBirthdate(userInfo.getBirthdate());
            result = true;
        }
        if (userInfo.getNickname() != null) {
            if(isUserNicknameDuplicated(userInfo.getNickname())){
                throw new NickNameDuplicatedException("Nickname Duplicated.");
            }
            user.setNickname(userInfo.getNickname());
            result = true;
        }
        if (userInfo.getProfileS3Key() != null) {
            String old = user.getProfileS3Key();
            log.info("old one:{}", old);
            user.setProfileS3Key(userInfo.getProfileS3Key()); // 프로필 이미지 URL 저장
            if(old != null) s3Service.deleteImageFromS3(old);
            result = true;
        }

        if (result) {
            userRepository.save(user);
        }
        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReviewResponseDTO> getMyReviews(String username) {
        User user = userRepository.findUserByEmailAndDeactivationDateIsNull(username).orElseThrow(() -> new ResourceNotFoundException("no active user :" + username));

        List<Review> reviewsByUserUserId = reviewRepository.findReviewsByUser_UserIdAndDeleteAtIsNull(user.getUserId());

        return reviewsByUserUserId.stream().map(Review::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateMyReview(Long reviewId, ReviewUpdateRequestDTO reviewUpdateRequestDTO, String username) {
        User user = userRepository.findUserByEmailAndDeactivationDateIsNull(username).orElseThrow(() -> new ResourceNotFoundException("no active user :" + username));

        Review review = reviewRepository.findByIdAndDeleteAtIsNull(reviewId).orElseThrow(() -> new ResourceNotFoundException("no review :" + reviewId));

        if(review.getUser().getUserId()!=user.getUserId()){
            throw new AccessDeniedException("you do not have permission to modify this review");
        }

        List<ReviewImg> newImgList = reviewUpdateRequestDTO.getImages().stream().map(img-> ReviewImg.builder().review(review).s3Key(img.getS3Key()).build()).collect(Collectors.toList());

        // 겹치지 않는 imgList의 img 삭제 로직
        review.getReviewImgList().stream().filter(img-> newImgList.stream().map(newImg-> newImg.getS3Key()).collect(Collectors.toList()).contains(img.getS3Key()))
                        .forEach(img-> s3Service.deleteImageFromS3(img.getS3Key()));

        review.update(reviewUpdateRequestDTO, newImgList);

        reviewRepository.save(review);

    }

    @Transactional
    @Override
    public void deleteMyReview(Long reviewId, String username) {
        User user = userRepository.findUserByEmailAndDeactivationDateIsNull(username).orElseThrow(() -> new ResourceNotFoundException("no active user :" + username));

        Review review = reviewRepository.findByIdAndDeleteAtIsNull(reviewId).orElseThrow(() -> new ResourceNotFoundException("no review :" + reviewId));

        if(review.getUser().getUserId()!=user.getUserId()){
            throw new AccessDeniedException("you do not have permission to delete this review");
        }

        review.getReviewImgList().stream().forEach(img-> s3Service.deleteImageFromS3(img.getS3Key()));

        reviewRepository.deleteReviewByReview(review);
    }

    /**
     * SecurityContextHolder에 저장된 유저 정보 추출
     * @return
     */
    private static String findUserEmailFromJwt() {
        String userEmail = "default@default.com";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            userEmail = authentication.getName();
        }
        return userEmail;
    }

    @Override
    public boolean isUserNicknameDuplicated(String nickname){
        log.info("nickname:{}", nickname);
        log.info("duplicated?:{}", userRepository.existsByNicknameNative(nickname));
        return userRepository.existsByNicknameNative(nickname);
    }



    @Override
    public void recoverData(User user) {
        user.setDeactivationDate(null);
        user.setSignupDate(LocalDateTime.now());
        // TODO 다른 테아블도 복구
    }

}
