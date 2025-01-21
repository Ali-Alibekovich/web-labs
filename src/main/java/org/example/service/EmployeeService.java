package org.example.service;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;


public interface EmployeeService {
    @GET
    Response searchEmployees(
            @QueryParam(value = "firstName") String firstName,
            @QueryParam(value = "lastName") String lastName,
            @QueryParam(value = "position") String position,
            @QueryParam(value = "minSalary") Double minSalary,
            @QueryParam(value = "maxSalary") Double maxSalary,
            @QueryParam(value = "department") String department
    );


    @POST
    Response createEmployee(
            @QueryParam(value = "firstName") String firstName,
            @QueryParam(value = "lastName") String lastName,
            @QueryParam(value = "position") String position,
            @QueryParam(value = "salary") Double salary,
            @QueryParam(value = "department") String department,
            @HeaderParam("Authorization") String authHeader
    );

    @PUT
    Response updateEmployee(
            @QueryParam(value = "id") int id,
            @QueryParam(value = "firstName") String firstName,
            @QueryParam(value = "lastName") String lastName,
            @QueryParam(value = "position") String position,
            @QueryParam(value = "salary") Double salary,
            @QueryParam(value = "department") String department,
            @HeaderParam("Authorization") String authHeader
    );

    @DELETE
    Response deleteEmployee(
            @QueryParam(value = "id") int id,
            @HeaderParam("Authorization") String authHeader
    );
}