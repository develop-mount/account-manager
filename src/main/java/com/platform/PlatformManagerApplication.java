package com.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaRepositories
@SpringBootApplication
@EnableScheduling
@EntityScan(basePackages = "com.platform.entity")
public class PlatformManagerApplication {
    
    static {
        System.setProperty("webdriver.chrome.driver", "chromedriver");
    }
    
    public static void main(String[] args) {
        SpringApplication.run(PlatformManagerApplication.class, args);
    }
} 