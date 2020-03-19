package com.space4u.mpkgen.util;

import com.space4u.mpkgen.entity.Role;
import com.space4u.mpkgen.repository.RoleRepository;

import java.util.ArrayList;
import java.util.List;

public enum RoleMappings {
    Employee {
        @Override
        public List<Role> mappingRoles(RoleRepository roleRepository) {
            List<Role> roles = new ArrayList<>();
            roles.add(roleRepository.findRoleByName("ROLE_EMPLOYEE"));
            return roles;
        }
    },
    Manager {
        @Override
        public List<Role> mappingRoles(RoleRepository roleRepository) {
            List<Role> roles = new ArrayList<>();
            roles.add(roleRepository.findRoleByName("ROLE_EMPLOYEE"));
            roles.add(roleRepository.findRoleByName("ROLE_MANAGER"));
            return roles;
        }
    },
    Admin {
        @Override
        public List<Role> mappingRoles(RoleRepository roleRepository) {
            List<Role> roles = new ArrayList<>();
            roles.add(roleRepository.findRoleByName("ROLE_EMPLOYEE"));
            roles.add(roleRepository.findRoleByName("ROLE_MANAGER"));
            roles.add(roleRepository.findRoleByName("ROLE_ADMIN"));
            return roles;
        }
    };

    public abstract List<Role> mappingRoles(RoleRepository roleRepository);

    public static String findRoleName(int size) {
        String name = null;
        switch (size) {
            case 1: name = Employee.name();
                break;
            case 2: name = Manager.name();
                break;
            case 3: name = Admin.name();
                break;
        }
        return name;
    }
}
