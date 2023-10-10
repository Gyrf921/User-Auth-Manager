package com.userauthmanager.backend.service;

import com.userauthmanager.backend.model.User;
import com.userauthmanager.backend.web.dto.JwtRequestDTO;
import com.userauthmanager.backend.web.dto.RegistrationUserDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {

    /**
     * Selected user from database by name
     * @param name user for find
     * @return user from database
     */
    User getUserByName(String name);

/*    *//**
     * Checks if such a user exists
     * @param jwtRequest name and password for checking
     *//*
    void checkToUserByAuthenticationManager(JwtRequestDTO jwtRequest);*/


    /**
     * Registration user and save him in database
     * @param registrationUserDTO user information for registration
     * @return jws token for user
     */
    String createUser(RegistrationUserDTO registrationUserDTO);

    @Transactional
    String setUserToSecurityAndCreateToken(String name);

    /**
     * NOT IMPLEMENTED | Check sesCode to correct
     * @param sesCode for checking
     * @return true - sesCode is corrected | false - sesCode uncorrected
     */
    Boolean userEmailConfirmation(String sesCode);

    /**
     * NOT IMPLEMENTED | Generated ses code for confirmation user Email
     * @param userName
     * @return
     */
    String createSesCode(String userName);

    /**
     * Delete user from database
     * @param userName for deleting
     */
    void deleteUser(String userName);

    /**
     * Method for Admin | Select all user from database
     * @return all user
     */
    List<User> getAllUser();

    /**
     * Checking correct password
     * @param registrationUserDTO - 2 password for checking
     * @return true - password is corrected | exception - password is uncorrected
     */
    Boolean isPasswordsMatch(RegistrationUserDTO registrationUserDTO);

}
