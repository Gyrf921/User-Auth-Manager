package com.userauthmanager.backend.service;

import com.userauthmanager.backend.model.Role;

import java.util.List;

public interface RoleService {

    /**
     * Find rple in database by name
     * @param roleName for finding
     * @return Role from database
     */
    Role getRoleByName(String roleName);

    /**
     * select all role from database
     * @return List of role
     */
    List<Role> getAllRole();

    /**
     * Create and save new role
     * @param roleName for new role
     * @return saved role
     */
    Role createRole(String roleName);
}
