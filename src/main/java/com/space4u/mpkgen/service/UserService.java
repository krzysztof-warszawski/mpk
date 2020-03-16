package com.space4u.mpkgen.service;

import com.space4u.mpkgen.api.CrmUser;
import com.space4u.mpkgen.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> findAll();
    User findByUserName(String userName);
    User findById(int id);
    void save(CrmUser crmUser);
    void deleteById(int id);
    List<User> searchBy(String name);

}
