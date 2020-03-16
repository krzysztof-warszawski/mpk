package com.space4u.mpkgen.repository;

import com.space4u.mpkgen.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findRoleByName(String name);
}
