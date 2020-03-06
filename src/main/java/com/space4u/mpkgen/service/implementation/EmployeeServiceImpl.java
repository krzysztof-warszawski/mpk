package com.space4u.mpkgen.service.implementation;

import com.space4u.mpkgen.api.request.AddEmployeeRequest;
import com.space4u.mpkgen.api.request.LoginRequest;
import com.space4u.mpkgen.model.Employee;
import com.space4u.mpkgen.repository.EmployeeRepository;
import com.space4u.mpkgen.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class defining CRUD operations on Employee objects
 */

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    @Override
    public Employee findEmployeeByLoginAndPassword(LoginRequest request) {
        return null;
    }

    @Override
    public List<Employee> findAll() {
        return null;
    }

    @Override
    public int addEmployee(AddEmployeeRequest request) {
        return 0;
    }

    @Override
    public void deleteEmployeeById(int employeeId) {

    }

    @Override
    public Employee getEmployeeById(int employeeId) {
        return null;
    }
}