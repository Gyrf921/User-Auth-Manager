package com.userauthmanager.backend.service.impl;

import com.userauthmanager.backend.exception.ResourceNotFoundException;
import com.userauthmanager.backend.model.Role;
import com.userauthmanager.backend.repository.RoleRepository;
import com.userauthmanager.backend.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByName(String roleName) {
        log.info("[getRoleByName] >> roleName: {}", roleName);

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> {
                    log.error("Role not found by this name :{} ", roleName);
                    return new ResourceNotFoundException("Role not found by this name :: " + roleName);
                });

        log.info("[getRoleByName] << result: {}", role);
        return role;
    }

    @Override
    public List<Role> getAllRole() {
        log.info("[getAllRole] >> without params");

        List<Role> role = roleRepository.findAll();

        log.info("[getAllRole] << result: {}", role);

        return role;
    }

    @Override
    public Role createRole(String roleName) {
        log.info("[createRole] >> without params");

        Role savedRole = roleRepository.save(
                Role.builder()
                        .name(roleName)
                        .build()
        );

        log.info("[createRole] << result: {}", savedRole);

        return savedRole;
    }
}
