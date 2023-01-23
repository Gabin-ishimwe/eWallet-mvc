package com.ewallet.userService.repositories;

import com.ewallet.userService.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String user);
}
