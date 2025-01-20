package org.example.service;

import org.example.model.Employee;
import javax.ws.rs.*;
import java.util.List;


public interface EmployeeService {
    @GET
    List<Employee> searchEmployees(
            @QueryParam(value = "firstName") String firstName,
            @QueryParam(value = "lastName") String lastName,
            @QueryParam(value = "position") String position,
            @QueryParam(value = "minSalary") Double minSalary,
            @QueryParam(value = "maxSalary") Double maxSalary,
            @QueryParam(value = "department") String department
    );
}