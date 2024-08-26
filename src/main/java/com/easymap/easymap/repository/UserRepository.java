package com.easymap.easymap.repository;

import com.easymap.easymap.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM users WHERE nickname=:nickname AND deactivation_date is null )", nativeQuery = true)
    boolean existsByNicknameNative(String nickname);

    Optional<User> findUserByEmailAndDeactivationDateIsNull(String email);

    Optional<User> findUserByUserIdAndDeactivationDateIsNull(Long userId);

    boolean existsUserByEmailAndDeactivationDateIsNull(String email);

}
