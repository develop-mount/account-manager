package com.platform.service;

import com.platform.entity.CookieEntity;
import com.platform.repository.CookieRepository;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.openqa.selenium.Cookie;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CookieService {
    private static final Map<String, String> PLATFORM_URLS = Map.of(
        "weibo", "https://weibo.com",
        "douyin", "https://www.douyin.com",
        "weixin", "https://channels.weixin.qq.com"
    );
    private static final Logger log = LoggerFactory.getLogger(CookieService.class);

    private final CookieRepository cookieRepository;
    private final ChromeOptions chromeOptions;

    @Transactional
    public void saveCookies(String platform, List<CookieEntity> cookies) {

        cookieRepository.deleteByPlatform(platform);
        cookies.forEach(cookie -> {
            cookie.setPlatform(platform);
            cookie.setLastRefreshed(LocalDateTime.now());
        });
        cookieRepository.saveAll(cookies);
    }

    public List<CookieEntity> getCookies(String platform) {
        return cookieRepository.findByPlatformAndIsValidTrue(platform);
    }

    @Scheduled(fixedRate = 300000)
    @Transactional
    public void refreshCookies() {
        Map<String, List<CookieEntity>> validCookiesByPlatform = cookieRepository.findByIsValidTrue().stream()
                .collect(Collectors.groupingBy(CookieEntity::getPlatform));
        ChromeDriver driver = new ChromeDriver(chromeOptions);

        try {
            for (Map.Entry<String, List<CookieEntity>> entry : validCookiesByPlatform.entrySet()) {

                List<CookieEntity> cookieEntityList = entry.getValue();
                try {
                    String url = PLATFORM_URLS.get(entry.getKey());
                    if (url != null) {

                        driver.get(url);

                        // 删除所有现有的cookie
                        driver.manage().deleteAllCookies();

                        for (CookieEntity cookieEntity : cookieEntityList) {
                            // 添加cookie
                            Cookie cookie = new Cookie.Builder(cookieEntity.getName(), cookieEntity.getValue())
                                    .domain(cookieEntity.getDomain())
                                    .path(cookieEntity.getPath())
                                    .isSecure(cookieEntity.getSecure())
                                    .build();

                            driver.manage().addCookie(cookie);
                        }

                        // 刷新页面
                        driver.navigate().refresh();

                        for (CookieEntity cookieEntity : cookieEntityList) {
                            cookieEntity.setLastRefreshed(LocalDateTime.now());
                        }
                    }
                } catch (Exception e) {
                    log.error("错误：{}", e.getMessage(), e);
                    for (CookieEntity cookieEntity : cookieEntityList) {
                        cookieEntity.setIsValid(false);
                    }
                }
                
                cookieRepository.saveAll(cookieEntityList);
            }
        } finally {
            driver.quit();
        }


    }
} 