package com.userauthmanager.backend.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtRequestDTO {

    @NotNull
    @Email
    private String userEmail;

    @NotNull
    @Length(min = 2, max = 30, message = "firstName length must be from 2 to 30")
    private String password;
}
