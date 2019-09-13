package com.farhutdinov.springboot2.springboot2jpacrudexample.controller;


import com.farhutdinov.springboot2.springboot2jpacrudexample.exception.ResourceNotFoundException;
import com.farhutdinov.springboot2.springboot2jpacrudexample.model.Employee;
import com.farhutdinov.springboot2.springboot2jpacrudexample.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1")
public class EmployeeController {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/employees")
    public List<Employee> getAll(){
        return employeeRepository.findAll();
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId) throws ResourceNotFoundException {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

        return ResponseEntity.ok().body(employee);
    }

    @PostMapping("/employees")
    public Employee createEmployee(@Valid @RequestBody Employee employee){
        return employeeRepository.save(employee);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee (@PathVariable(value = "id") Long employeeId,
                                                    @Valid @RequestBody Employee employeeDetails) throws ResourceNotFoundException{
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

        employee.setEmailId(employeeDetails.getEmailId());
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());

        final Employee updatedEmployee = employeeRepository.save(employee);

        return ResponseEntity.ok(updatedEmployee);
    }
    @DeleteMapping("/employees/{id}")
    public Map<String,Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId) throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

        employeeRepository.delete(employee);
        Map<String,Boolean> response = new HashMap<>();
        response.put("delete",Boolean.TRUE);
        return response;
    }
}
