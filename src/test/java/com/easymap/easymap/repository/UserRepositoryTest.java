package com.easymap.easymap.repository;

import com.easymap.easymap.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev") // 로컬 DB로 테스트 돌리기 위해 프로파일 추가함.
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @BeforeEach
    public void setUp(){
        IntStream.rangeClosed(1, 50)
                .forEach(i-> {
                    User user = new User();
                    user.setNickname("user"+i);

                    userRepository.save(user);
                });

    }

    @Transactional
    @AfterEach
    public void cleanUp(){
        userRepository.deleteAll();
    }

    @Test
    public void existsByNicknameSuccessTest(){

        boolean isExists = userRepository.existsByNicknameNative("user1");

        Assertions.assertTrue(isExists);

    }
    @Test
    public void existsByNicknameFailTest(){

        boolean isExists = userRepository.existsByNicknameNative("user100");

        Assertions.assertFalse(isExists);

    }


}