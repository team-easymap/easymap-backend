package com.easymap.easymap.entity;

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
    @Column(name = "profile_s3_url")
    private String profileS3Url;

}
