package com.easymap.easymap.repository;

//import com.easymap.easymap.entity.User;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.Date;
//import java.util.Optional;
//import java.util.stream.IntStream;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@ActiveProfiles("dev") // 로컬 DB로 테스트 돌리기 위해 프로파일 추가함.
class UserRepositoryTest {

//    @Autowired
//    private UserRepository userRepository;
//
//    @Transactional
//    @BeforeEach
//    public void setUp(){
//        IntStream.rangeClosed(1, 50)
//                .forEach(i-> {
//                    User user = new User();
//                    user.setNickname("user"+i);
//                    user.setEmail("user"+i+"@gmail.com");
//
//                    if(i%4==0){
//                        user.setDeactivationDate(LocalDateTime.now().minusMinutes(4L));
//                    }
//
//                    userRepository.save(user);
//                });
//
//    }
//
//    @Transactional
//    @AfterEach
//    public void cleanUp(){
//        userRepository.deleteAll();
//    }
//
//    @Test
//    public void existsByNicknameSuccessTest(){
//
//        boolean isExists = userRepository.existsByNicknameNative("user1");
//
//        Assertions.assertTrue(isExists);
//
//    }
//    @Test
//    public void existsByNicknameFailTest(){
//
//        boolean isExists = userRepository.existsByNicknameNative("user100");
//
//        Assertions.assertFalse(isExists);
//
//    }
//
//    @Test
//    public void 이메일로_탈퇴하지않은_회원찾기(){
//        Optional<User> userByEmailAndDeactivationDateIsNull = userRepository.findUserByEmailAndDeactivationDateIsNull("user6@gmail.com");
//
//        Assertions.assertDoesNotThrow(()-> userByEmailAndDeactivationDateIsNull.get());
//
//        Assertions.assertEquals(userByEmailAndDeactivationDateIsNull.get().getEmail(), "user6@gmail.com");
//        Assertions.assertNull(userByEmailAndDeactivationDateIsNull.get().getDeactivationDate());
//
//    }
//
//
//    @Test
//    public void 이메일로_탈퇴한_회원찾은경우(){
//        Optional<User> userByEmailAndDeactivationDateIsNull = userRepository.findUserByEmailAndDeactivationDateIsNull("user4@gmail.com");
//
//        Assertions.assertFalse(userByEmailAndDeactivationDateIsNull.isPresent());
//
//    }
//
//
//    @Test
//    public void 이메일로_탈퇴했는데_다시가입한_회원찾기(){
//        Optional<User> userByEmailAndDeactivationDateIsNull = userRepository.findUserByEmailAndDeactivationDateIsNull("user8@gmail.com");
//
//        Assertions.assertFalse(userByEmailAndDeactivationDateIsNull.isPresent());
//
//        User newUser = new User();
//        newUser.setNickname("user8");
//        newUser.setEmail("user8@gmail.com");
//        userRepository.save(newUser);
//
//        Optional<User> newFindedUser = userRepository.findUserByEmailAndDeactivationDateIsNull(newUser.getEmail());
//
//        Assertions.assertDoesNotThrow(()-> newFindedUser.get());
//
//        Assertions.assertEquals(newFindedUser.get().getEmail(), newUser.getEmail());
//        Assertions.assertNull(newFindedUser.get().getDeactivationDate());
//
//
//    }

}