package com.userauthmanager.backend.service.impl;

import com.userauthmanager.backend.exception.UserAlreadyExistException;
import com.userauthmanager.backend.mapper.UserMapper;
import com.userauthmanager.backend.model.User;
import com.userauthmanager.backend.repository.UserRepository;
import com.userauthmanager.backend.service.RoleService;
import com.userauthmanager.backend.service.UserService;
import com.userauthmanager.backend.utils.JwtTokenUtils;
import com.userauthmanager.backend.web.dto.request.RegistrationUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    public User getUserByEmail(String email) {
        log.info("[getUserByEmail] >> email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found by this email :{} ", email);
                    return new UsernameNotFoundException("User not found by this email :: " + email);
                });

        log.info("[getUserByEmail] << result: {}", user.getUserName());

        return user;
    }

    @Override
    @Transactional
    public String createUser(RegistrationUserDTO registrationUserDTO) {
        log.info("[createUser] >> name: {}", registrationUserDTO.getUserName());

        if (userRepository.findByEmail(registrationUserDTO.getUserName()).isPresent()) {
            log.error("User with this name already exist : {}", registrationUserDTO.getUserName());
            throw new UserAlreadyExistException("User with this name already exist :" + registrationUserDTO.getUserName());
        }

        User user = userMapper.registrationUserDTOToUser(registrationUserDTO);
        user.setPassword(passwordEncoder.encode(registrationUserDTO.getPassword()));
        user.setRoles(List.of(roleService.getRoleByName("ROLE_USER")));

        User savedUser = userRepository.save(user);

        log.info("[createUser] << result is token for user, savedUser name is : {}", savedUser.getUserName());

        return createTokenForUser(savedUser.getEmail());
    }

    @Override
    @Transactional
    public String createTokenForUser(String email){
        log.info("[setUserToSecurityAndCreateToken] >> create token for email: {}", email);

        UserDetails userDetails = loadUserByUsername(email);

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
    public void deleteUser(String userEmail) {
        log.info("[deleteUser] >> userEmail: {}", userEmail);

        userRepository.delete(getUserByEmail(userEmail));

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
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUserByEmail(email); // set email because email is unique

        //We give the name, password and map the role to the spring
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
    }
}
