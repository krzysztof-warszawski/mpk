package com.space4u.mpkgen.service.implementation;

import com.space4u.mpkgen.api.CrmUser;
import com.space4u.mpkgen.entity.Role;
import com.space4u.mpkgen.entity.User;
import com.space4u.mpkgen.repository.RoleRepository;
import com.space4u.mpkgen.repository.UserRepository;
import com.space4u.mpkgen.service.UserService;
import com.space4u.mpkgen.util.mappings.RoleMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public static int sessionUserId;

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    @Transactional
    public void save(CrmUser crmUser, boolean isNewUser) {
        RoleMappings roleList = RoleMappings.valueOf(RoleMappings.class, crmUser.getRoles());
        User user;
        if (isNewUser) {
            user = new User();
        } else {
            user = findById(crmUser.getUserId());
        }
        user.setUserName(crmUser.getUserName());
        user.setPassword(passwordEncoder.encode(crmUser.getPassword()));
        user.setFirstName(crmUser.getFirstName());
        user.setLastName(crmUser.getLastName());
        user.setRoles(roleList.mappingRoles(roleRepository));

        userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAllByOrderByLastNameAsc();
    }

    @Override
    public User findById(int id) {
        Optional<User> result = userRepository.findById(id);
        User user;

        if (result.isPresent()) {
            user = result.get();
        } else {
            throw new RuntimeException("Didn't find user id - " + id);
        }

        return user;
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> searchBy(String name) {
        List<User> results;

        if (name != null && name.trim().length() > 0) {
            results = userRepository.findByFirstNameContainsOrLastNameContainsAllIgnoreCase(name, name);
        } else {
            results = findAll();
        }

        return results;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        sessionUserId = user.getId();
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}