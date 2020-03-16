package com.space4u.mpkgen.repository;

import com.space4u.mpkgen.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findAllByOrderByLastNameAsc();

    User findByUserName(String userName);

//    void saveUser(User user);

    List<User> findByFirstNameContainsOrLastNameContainsAllIgnoreCase(String name, String lName);
}
