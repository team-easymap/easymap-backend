package com.easymap.easymap.service;

import com.easymap.easymap.dto.request.review.ReviewUpdateRequestDTO;
import com.easymap.easymap.dto.request.user.UserNicknameDuplicateRequestDTO;
import com.easymap.easymap.dto.request.user.UserRequiredInfoRequestDto;
import com.easymap.easymap.dto.response.review.ReviewResponseDTO;
import com.easymap.easymap.entity.Review;
import com.easymap.easymap.entity.ReviewImg;
import com.easymap.easymap.entity.User;
import com.easymap.easymap.handler.exception.ResourceNotFoundException;
import com.easymap.easymap.repository.ReviewRepository;
import com.easymap.easymap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
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

    @Override
    public boolean userNicknameDuplicateCheck(UserNicknameDuplicateRequestDTO userNicknameDuplicateRequestDTO) {
        return userRepository.existsByNicknameNative(userNicknameDuplicateRequestDTO.getNickname());

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
        User user = userRepository.findByEmail(userEmail).orElseThrow(
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

    @Override
    public boolean patchUserRequiredInfo(UserRequiredInfoRequestDto userInfo) {

        log.info("Received User Info: {}", userInfo);
        String userEmail = findUserEmailFromJwt();
        User user = userRepository.findByEmail(userEmail).orElseThrow(
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
            user.setNickname(userInfo.getNickname());
            result = true;
        }
        if (userInfo.getProfileS3Key() != null) {
            user.setProfileS3Key(userInfo.getProfileS3Key()); // 프로필 이미지 URL 저장
            result = true;
        }

        if (result) {
            userRepository.save(user);
        }
        return result;
    }

    @Override
    public List<ReviewResponseDTO> getMyReviews(String username) {
        User user = userRepository.findUserByEmailAndDeactivationDateIsNull(username).orElseThrow(() -> new ResourceNotFoundException("no active user :" + username));

        List<Review> reviewsByUserUserId = reviewRepository.findReviewsByUser_UserId(user.getUserId());


        return reviewsByUserUserId.stream().map(Review::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateMyReview(Long reviewId, ReviewUpdateRequestDTO reviewUpdateRequestDTO, String username) {
        User user = userRepository.findUserByEmailAndDeactivationDateIsNull(username).orElseThrow(() -> new ResourceNotFoundException("no active user :" + username));

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ResourceNotFoundException("no review :" + reviewId));

        if(review.getUser().getUserId()!=user.getUserId()){
            throw new AccessDeniedException("you do not have permission to modify this review");
        }
        // TODO 이미지 처리 후 넣어주기
        //reviewUpdateRequestDTO.getImageList();
        List<ReviewImg> imgList = new ArrayList<>();
        review.update(reviewUpdateRequestDTO, imgList);

        reviewRepository.save(review);

    }

    @Override
    public void deleteMyReview(Long reviewId, String username) {
        User user = userRepository.findUserByEmailAndDeactivationDateIsNull(username).orElseThrow(() -> new ResourceNotFoundException("no active user :" + username));

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ResourceNotFoundException("no review :" + reviewId));

        if(review.getUser().getUserId()!=user.getUserId()){
            throw new AccessDeniedException("you do not have permission to delete this review");
        }


        reviewRepository.delete(review);
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

}
