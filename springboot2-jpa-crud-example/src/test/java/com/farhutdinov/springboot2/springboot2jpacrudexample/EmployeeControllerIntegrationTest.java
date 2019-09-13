package com.farhutdinov.springboot2.springboot2jpacrudexample;


import com.farhutdinov.springboot2.springboot2jpacrudexample.exception.ErrorDetails;
import com.farhutdinov.springboot2.springboot2jpacrudexample.exception.ResourceNotFoundException;
import com.farhutdinov.springboot2.springboot2jpacrudexample.model.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.util.logging.Logger;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Springboot2JpaCrudExampleApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String getRootUrl(){
        return "http://localhost:" + port +"/api/v1";
    }


    @Test
    public void contextLoads(){

    }

    @Test
    public void getAllEmployees(){
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null,headers);

        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/employees", HttpMethod.GET,entity,String.class);
        assertEquals(200,response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetEmployeeById() {
        Employee employee = restTemplate.getForObject(getRootUrl() + "/employees/2", Employee.class);
        assertNotNull(employee);
        Logger.getGlobal().info(employee.toString());
    }

    @Test
    public void tesGetEmployeeByNonexistentId(){


            Logger.getGlobal().info( restTemplate.getForObject(getRootUrl() + "/employees/2",String.class));

        ErrorDetails message = restTemplate.getForObject(getRootUrl() + "/employees/2",ErrorDetails.class);

            assertEquals("Employee not found for this id :: 2",message.getMessage());

    }

    @Test
    public void testCreateEmployee() {
        Employee employee = new Employee();
        employee.setEmailId("admin@gmail.com");
        employee.setFirstName("admin");
        employee.setLastName("admin");

        ResponseEntity<Employee> postResponse = restTemplate.postForEntity(getRootUrl() + "/employees", employee, Employee.class);
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
    }

    @Test
    public void testUpdateEmployee() {
        int id = 1;
        Employee employee = restTemplate.getForObject(getRootUrl() + "/employees/" + id, Employee.class);
        employee.setFirstName("adminsNameWasUpdate");
        employee.setLastName("adminsLastNameWasUpdate");

        restTemplate.put(getRootUrl() + "/employees/" + id, employee);

        Employee updatedEmployee = restTemplate.getForObject(getRootUrl() + "/employees/" + id, Employee.class);
        assertNotNull(updatedEmployee);
    }

    @Test
    public void testDeleteEmployee() {
        int id = 1;

        Employee employee = restTemplate.getForObject(getRootUrl() + "/employees/" + id, Employee.class);
        assertNotNull(employee);
        Logger.getGlobal().info(employee.toString());

        restTemplate.delete(getRootUrl() + "/employees/" + id);

        try {
            employee = restTemplate.getForObject(getRootUrl() + "/employees/" + id, Employee.class);
        } catch (final HttpClientErrorException e) {
            assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }
    }
}
