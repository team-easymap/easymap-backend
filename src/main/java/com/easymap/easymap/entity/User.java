package com.easymap.easymap.entity;

import com.easymap.easymap.dto.response.review.ReviewImgResponseDTO;
import com.easymap.easymap.dto.response.user.LoadUserStatusResponseDto;
import com.easymap.easymap.dto.response.user.UserDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;
    private LocalDate birthdate;
    private Character gender;
    private String nickname;
    private String oauthType;
    private LocalDateTime signupDate;
    private String userRole;
    private LocalDateTime deactivationDate;
    @Column(name = "profile_s3_key")
    private String profileS3Key;

    public static UserDto mapToDTO(User user){
        return UserDto.builder()
                .userId(user.getUserId())
                .profileS3Key(user.getProfileS3Key())
                .email(user.getEmail())
                .gender(user.getGender())
                .birthdate(user.getBirthdate())
                .nickname(user.getNickname())
                .oauthType(user.getOauthType())
                .signupDate(user.getSignupDate())
                .build();
    }
}
