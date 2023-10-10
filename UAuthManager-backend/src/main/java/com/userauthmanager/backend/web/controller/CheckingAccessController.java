package com.userauthmanager.backend.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/check")
public class CheckingAccessController {

    @GetMapping("/unsecure")
    public String unsecureData() {
        return "unsecure Data (don't need authorization)";
    }

    @GetMapping("/secure")
    public String secureData() {
        return "secure Data for user and admin (need authorization)";
    }

    @GetMapping("/admin")
    public String adminData() {
        return "admin Data only for admin";
    }
}
