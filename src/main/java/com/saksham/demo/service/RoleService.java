package com.saksham.demo.service;

import org.springframework.beans.PropertyValues;

import com.saksham.demo.model.Role;

import java.util.List;

public interface RoleService {
    Role createRole(Role role);

    List<Role> findAll();
}
