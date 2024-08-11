package com.easymap.easymap.service;

import com.easymap.easymap.dto.request.user.UserNicknameDuplicateRequestDTO;
import com.easymap.easymap.entity.User;
import com.easymap.easymap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;


    @Override
    public boolean userNicknameDuplicateCheck(UserNicknameDuplicateRequestDTO userNicknameDuplicateRequestDTO) {
        return userRepository.existsByNicknameNative(userNicknameDuplicateRequestDTO.getNickname());

    }

    @Override
    public void userWithdraw(UserDetails userDetails) throws NoSuchElementException {
        Optional<User> foundUser = userRepository.findUserByEmailAndDeactivationDateIsNull(userDetails.getUsername());

        User user = foundUser.orElseThrow(() -> new NoSuchElementException());

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

    /**
     * SecurityContextHolder에 저장된 유저 정보 추출
     * @return
     */
    private String findUserEmailFromJwt() {
        String userEmail = "default@default.com";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            userEmail = authentication.getName();
        }
        return userEmail;
    }

}
