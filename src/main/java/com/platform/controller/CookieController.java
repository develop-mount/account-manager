package com.platform.controller;

import com.platform.entity.CookieEntity;
import com.platform.service.CookieService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cookies")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CookieController {
    private static final Logger log = LoggerFactory.getLogger(CookieController.class);
    private final CookieService cookieService;

    @PostMapping("/{platform}")
    public ResponseEntity<Void> saveCookies(
            @PathVariable String platform,
            @RequestBody Map<String, List<CookieEntity>> request) {
        log.info("{} cookie:{}", platform, request.get("cookies"));
        cookieService.saveCookies(platform, request.get("cookies"));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{platform}")
    public ResponseEntity<List<CookieEntity>> getCookies(@PathVariable String platform) {
        List<CookieEntity> cookies = cookieService.getCookies(platform);
        return ResponseEntity.ok(cookies);
    }
} 