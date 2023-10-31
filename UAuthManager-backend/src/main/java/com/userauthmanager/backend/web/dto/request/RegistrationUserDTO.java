package com.userauthmanager.backend.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationUserDTO {
    //Todo пфттерны на буквы
    @NotNull
    @Length(min = 2, max = 30, message = "firstName length must be from 2 to 30")
    private String userName;

    @NotNull
    @Length(min = 8, max = 30, message = "password length must be from 8 to 30")
    private String password;

    @NotNull
    @Email
    private String email;

}
