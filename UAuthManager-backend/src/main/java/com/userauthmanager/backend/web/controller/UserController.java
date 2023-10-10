package com.userauthmanager.backend.web.controller;


import com.userauthmanager.backend.model.User;
import com.userauthmanager.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    @GetMapping("/info")
    public ResponseEntity<User> getInformationAboutUser(Principal principal) {
        log.info("[getInformationAboutUser] >> create token for name: {}", principal.getName());

        User user = userService.getUserByName(principal.getName());

        log.info("[getInformationAboutUser] << result: {}", user.getUserName());

        return ResponseEntity.ok().body(user);
    }

}
