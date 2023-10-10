package com.userauthmanager.backend.web.controller;

import com.userauthmanager.backend.model.User;
import com.userauthmanager.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.TreeSet;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("[getAllUsers] >> no params");

        int d = 5;
        List<User> users = userService.getAllUser();

        log.info("[getAllUsers] << result : {}", users.size());

        return ResponseEntity.ok().body(users);
    }
}
