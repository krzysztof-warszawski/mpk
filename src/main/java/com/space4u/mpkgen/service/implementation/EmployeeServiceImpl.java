package com.space4u.mpkgen.service.implementation;

import com.space4u.mpkgen.entity.Employee;
import com.space4u.mpkgen.repository.EmployeeRepository;
import com.space4u.mpkgen.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class defining CRUD operations on Employee objects
 */

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAllByOrderByLastNameAsc();
    }

    @Override
    public Employee findById(int id) {
        Optional<Employee> result = employeeRepository.findById(id);

        Employee employee;

        if (result.isPresent()) {
            employee = result.get();
        } else {
            throw new RuntimeException("Didn't find employee id - " + id);
        }

        return employee;
    }

    @Override
    public void save(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public void deleteById(int id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public List<Employee> searchBy(String name) {
        List<Employee> results;

        if (name != null && name.trim().length() > 0) {
            results = employeeRepository.findByFirstNameContainsOrLastNameContainsAllIgnoreCase(name, name);
        } else {
            results = findAll();
        }

        return results;
    }
}