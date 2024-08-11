package com.easymap.easymap.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Table(name = "notifications")
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @CreatedDate
    private LocalDateTime notificationDate;

    private String notificationType;

    private String notificationText;

    private LocalDateTime deletedAt;

}
