package com.platform.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "cookies")
@Data
public class CookieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String platform;

    private String name;

    private String value;

    private String domain;

    private String path;

    private Boolean secure;

    private Boolean httpOnly;

    private String sameSite;

    private Long expirationDate;

    private LocalDateTime lastRefreshed;

    private Boolean isValid = true;
} 