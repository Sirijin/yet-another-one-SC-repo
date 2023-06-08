package com.example.demo.repository;

import com.example.demo.entity.Role;
import com.example.demo.type.RoleSet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleSet role);
}
