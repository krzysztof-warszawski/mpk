package com.space4u.mpkgen.service;

import com.space4u.mpkgen.api.request.AddEmployeeRequest;
import com.space4u.mpkgen.api.request.LoginRequest;
import com.space4u.mpkgen.model.Employee;

import java.util.List;

public interface EmployeeService {

    Employee findEmployeeByLoginAndPassword(LoginRequest request);
    List<Employee> findAll();
    int addEmployee(AddEmployeeRequest request);
    void deleteEmployeeById(int employeeId);
    Employee getEmployeeById(int employeeId);
}
