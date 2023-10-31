package com.userauthmanager.backend.mapper;

import com.userauthmanager.backend.model.User;
import com.userauthmanager.backend.web.dto.request.RegistrationUserDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    User registrationUserDTOToUser(RegistrationUserDTO requestDTO);

}
