package com.space4u.mpkgen.repository;

import com.space4u.mpkgen.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Employee findEmployeeByLoginAndPassword(String login, String password);
}
