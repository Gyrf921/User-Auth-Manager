package com.userauthmanager.backend.web.controller;

import com.userauthmanager.backend.service.UserService;
import com.userauthmanager.backend.web.dto.request.JwtRequestDTO;
import com.userauthmanager.backend.web.dto.request.RegistrationUserDTO;
import com.userauthmanager.backend.web.dto.response.JwtResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    @Operation(summary = "Authentication and create Token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Validation failed for some argument. Invalid input supplied"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping("/authentication")
    public ResponseEntity<JwtResponseDTO> createToken(@RequestBody JwtRequestDTO jwtRequest) {
        log.info("[createToken] >> create token for email: {}", jwtRequest.getUserEmail());

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUserEmail(), jwtRequest.getPassword()));
        } catch (BadCredentialsException badCredentialsException) {
            log.error(badCredentialsException.getMessage());
            throw badCredentialsException;
        }
        //Проверка на сузествование
        String token = userService.createTokenForUser(jwtRequest.getUserEmail());

        log.info("[createToken] << result is token");
        return ResponseEntity.ok().body(new JwtResponseDTO(token));
    }

    @Operation(summary = "Registration and create Token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Validation failed for some argument. Invalid input supplied"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping("/registration")
    public ResponseEntity<JwtResponseDTO> createUser(@RequestBody RegistrationUserDTO registrationUserDTO) {
        log.info("[createUser] >> create user with name: {}", registrationUserDTO.getUserName());

        String token = userService.createUser(registrationUserDTO);

        log.info("[createUser] << result is token");

        return ResponseEntity.ok().body(new JwtResponseDTO(token));
    }

}
