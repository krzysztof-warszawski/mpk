package com.space4u.mpkgen.service;

import com.space4u.mpkgen.entity.Employee;

import java.util.List;

public interface EmployeeService {

//    Employee findEmployeeByLoginAndPassword(LoginRequest request);
//    List<Employee> findAll();
//    int addEmployee(AddEmployeeRequest request);
//    void deleteEmployeeById(int employeeId);
//    Employee getEmployeeById(int employeeId);

    List<Employee> findAll();
    Employee findById(int id);
    void save(Employee employee);
    void deleteById(int id);
    List<Employee> searchBy(String name);

}
