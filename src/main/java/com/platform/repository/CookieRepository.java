package com.platform.repository;

import com.platform.entity.CookieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CookieRepository extends JpaRepository<CookieEntity, Long> {
    List<CookieEntity> findByPlatformAndIsValidTrue(String platform);
    List<CookieEntity> findByIsValidTrue();;
    int deleteByPlatform(String platform);
}