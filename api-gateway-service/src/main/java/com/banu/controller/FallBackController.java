package com.banu.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallBackController {

    @GetMapping("/authservice")
    public ResponseEntity<String> authServiceFallBack() {
        return ResponseEntity.ok("Auth service şu anda hizmet dışıdır.");
    }

    @GetMapping("/userservice")
    public ResponseEntity<String> userServiceFallBack() {
        return ResponseEntity.ok("User service şu anda hizmet dışıdır.");
    }
}
