package com.easymap.easymap.service;

import com.easymap.easymap.dto.request.user.UserNicknameDuplicateRequestDTO;
import com.easymap.easymap.entity.User;
import com.easymap.easymap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
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
}
