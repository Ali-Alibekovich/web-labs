package org.example.service;

import org.example.model.Employee;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface EmployeeService {
    @WebMethod
    List<Employee> searchEmployees(
            @WebParam(name = "firstName") String firstName,
            @WebParam(name = "lastName") String lastName,
            @WebParam(name = "position") String position,
            @WebParam(name = "minSalary") Double minSalary,
            @WebParam(name = "maxSalary") Double maxSalary,
            @WebParam(name = "department") String department
    );
}