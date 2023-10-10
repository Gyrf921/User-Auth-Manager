package com.userauthmanager.backend.service.impl;

import com.userauthmanager.backend.exception.BadLoginOrPasswordException;
import com.userauthmanager.backend.exception.UserAlreadyExistException;
import com.userauthmanager.backend.mapper.UserMapper;
import com.userauthmanager.backend.model.User;
import com.userauthmanager.backend.repository.UserRepository;
import com.userauthmanager.backend.service.RoleService;
import com.userauthmanager.backend.service.UserService;
import com.userauthmanager.backend.utils.JwtTokenUtils;
import com.userauthmanager.backend.web.dto.JwtRequestDTO;
import com.userauthmanager.backend.web.dto.RegistrationUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserDetailsService, UserService {

    private final JwtTokenUtils jwtTokenUtils;

    private final UserMapper userMapper;

    private final RoleService roleService;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public User getUserByName(String name) {

        log.info("[getUserByName] >> name: {}", name);

        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> {
                    log.error("User not found by this name :{} ", name);
                    return new UsernameNotFoundException("User not found by this name :: " + name);
                });

        log.info("[getUserByName] << result: {}", user.getUserName());

        return user;
    }

/*    @Override

    public void checkToUserByAuthenticationManager(JwtRequestDTO jwtRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUserName(), jwtRequest.getPassword()));
        } catch (BadCredentialsException badCredentialsException) {
            log.error(badCredentialsException.getMessage());
            throw badCredentialsException;
        }
    }
*/

    @Override
    @Transactional
    public String createUser(RegistrationUserDTO registrationUserDTO) {
        log.info("[createUser] >> name: {}", registrationUserDTO.getUserName());

        isPasswordsMatch(registrationUserDTO);

        if (userRepository.findByUserName(registrationUserDTO.getUserName()).isPresent()) {
            log.error("Пользователь с таким именем уже существует : {}", registrationUserDTO.getUserName());
            throw new UserAlreadyExistException("Пользователь с таким именем уже существует :" + registrationUserDTO.getUserName());
        }

        User user = userMapper.registrationUserDTOToUser(registrationUserDTO);
        user.setPassword(passwordEncoder.encode(registrationUserDTO.getPassword()));
        user.setRoles(List.of(roleService.getRoleByName("ROLE_USER")));

        User savedUser = userRepository.save(user);

        log.info("[createUser] << result is token for user");

        return setUserToSecurityAndCreateToken(savedUser.getUserName());
    }

    @Transactional
    @Override
    public String setUserToSecurityAndCreateToken(String name){
        log.info("[setUserToSecurityAndCreateToken] >> create token for name: {}", name);

        UserDetails userDetails = loadUserByUsername(name);

        log.info("[setUserToSecurityAndCreateToken] << result is token");

        return jwtTokenUtils.generateToken(userDetails);
    }

    @Override
    public Boolean userEmailConfirmation(String sesCode) {
        return false;
    }

    @Override
    public String createSesCode(String userName) {
        return null;
    }

    @Override
    public void deleteUser(String userName) {
        log.info("[deleteUser] >> name: {}", userName);

        userRepository.delete(getUserByName(userName));

        log.info("[deleteUser] << result: user has been deleted");
    }

    @Override
    public List<User> getAllUser() {
        log.info("[getAllUser] without params");

        List<User> users = userRepository.findAll();

        log.info("[getAllUser] << result: {}", users.size());

        return users;
    }

    @Override
    public Boolean isPasswordsMatch(RegistrationUserDTO registrationUserDTO) {
        if (!registrationUserDTO.getPassword().equals(registrationUserDTO.getConfirmPassword())) {
            log.error("Пароли не совпадают:");
            throw new BadLoginOrPasswordException("Пароли не совпадают");
        }
        return true;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByName(username);

        //Даём имя, пароль и мапим роль в спринговую
        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
    }
}
