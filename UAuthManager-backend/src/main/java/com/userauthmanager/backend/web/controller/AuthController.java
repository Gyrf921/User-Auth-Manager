package com.userauthmanager.backend.web.controller;

import com.userauthmanager.backend.service.UserService;
import com.userauthmanager.backend.web.dto.JwtRequestDTO;
import com.userauthmanager.backend.web.dto.JwtResponseDTO;
import com.userauthmanager.backend.web.dto.RegistrationUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;


    private final AuthenticationManager authenticationManager;

    //TODO веб документация
    @PostMapping("/authentication")
    public ResponseEntity<JwtResponseDTO> createToken(@RequestBody JwtRequestDTO jwtRequest) {
        log.info("[createToken] >> create token for name: {}", jwtRequest.getUserName());

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUserName(), jwtRequest.getPassword()));
        } catch (BadCredentialsException badCredentialsException) {
            log.error(badCredentialsException.getMessage());
            throw badCredentialsException;
        }

        String token = userService.setUserToSecurityAndCreateToken(jwtRequest.getUserName());

        log.info("[createToken] << result is token");
        return ResponseEntity.ok().body(new JwtResponseDTO(token));
    }

    @PostMapping("/registration")
    public ResponseEntity<JwtResponseDTO> createUser(@RequestBody RegistrationUserDTO registrationUserDTO) {
        log.info("[createUser] >> create user with name: {}", registrationUserDTO.getUserName());

        String token = userService.createUser(registrationUserDTO);

        log.info("[createUser] << result is token");

        return ResponseEntity.ok().body(new JwtResponseDTO(token));
    }

}
