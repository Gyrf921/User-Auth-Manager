package com.userauthmanager.backend.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationUserDTO {

    private String userName;

    private String password;

    private String confirmPassword;

    private String email;

}
