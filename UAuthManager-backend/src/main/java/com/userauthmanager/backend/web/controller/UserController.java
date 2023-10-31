package com.userauthmanager.backend.web.controller;


import com.userauthmanager.backend.model.User;
import com.userauthmanager.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get information about authorized user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Validation failed for some argument. Invalid input supplied"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @GetMapping("/info")
    public ResponseEntity<User> getInformationAboutUser(Principal principal) {
        log.info("[getInformationAboutUser] >> create token for email: {}", principal.getName());

        User user = userService.getUserByEmail(principal.getName());

        log.info("[getInformationAboutUser] << result: {}", user.getUserName());

        return ResponseEntity.ok().body(user);
    }

}
