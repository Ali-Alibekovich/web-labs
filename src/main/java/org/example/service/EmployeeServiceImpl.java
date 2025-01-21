package org.example.service;

import org.example.model.Employee;
import org.example.model.dao.EmployeeDAO;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.List;

import static org.example.DbProvider.getDataSource;


@Path("/employee")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeServiceImpl implements EmployeeService {

    Connection conn;

    EmployeeDAO dao;

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password";

    public EmployeeServiceImpl() {
        try {
            this.conn = getDataSource().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            Statement st = conn.createStatement();
            st.execute(" CREATE TABLE IF NOT EXISTS employees ( " +
                    "id INT PRIMARY KEY, " +
                    "first_name VARCHAR(50), " +
                    "last_name VARCHAR(50), " +
                    "position VARCHAR(50), " +
                    "salary DECIMAL(10,2), " +
                    "department VARCHAR(50));");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        dao = new EmployeeDAO(conn);
    }

    private boolean authenticate(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return false;
        }
        String base64Credentials = authHeader.substring("Basic ".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        String[] values = credentials.split(":", 2);
        return values.length == 2 && USERNAME.equals(values[0]) && PASSWORD.equals(values[1]);
    }

    private Response unauthorized() {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("User is not authorized.")
                .build();
    }


    @Override
    public Response searchEmployees(String firstName,
                                    String lastName,
                                    String position,
                                    Double minSalary,
                                    Double maxSalary,
                                    String department) {
        try {
            List<Employee> employees = dao.searchEmployees(firstName, lastName, position, minSalary, maxSalary, department);
            return Response.status(Response.Status.OK).entity(employees).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error searching employee").build();
        }
    }

    @Override
    public Response createEmployee(String firstName, String lastName, String position, Double salary, String department, String authHeader) {
        if (!authenticate(authHeader)) {
            return unauthorized();
        }

        if (firstName == null || firstName.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("First name cannot be null or empty").build();
        }

        try {
            Employee employee = new Employee(0, firstName, lastName, position, salary, department);
            dao.createEmployee(employee);

            return Response.status(Response.Status.CREATED).entity("Employee created with id " + employee.getId()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating employee").build();
        }
    }

    @Override
    public Response updateEmployee(int id, String firstName, String lastName, String position, Double salary, String department, String authHeader) {
        if (!authenticate(authHeader)) {
            return unauthorized();
        }
        try {
            if (dao.employeeExists(id)) {
                return Response.status(Response.Status.NOT_FOUND).entity("Employee with id " + id + " not found").build();
            }

            Employee employee = new Employee(id, firstName, lastName, position, salary, department);
            dao.updateEmployee(id, employee);

            return Response.status(Response.Status.CREATED).entity("Employee updated with id " + employee.getId()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating employee").build();
        }
    }

    @Override
    public Response deleteEmployee(int id, String authHeader) {
        if (!authenticate(authHeader)) {
            return unauthorized();
        }
        try {
            if (dao.employeeExists(id)) {
                return Response.status(Response.Status.NOT_FOUND).entity("Employee with id " + id + " not found").build();
            }
            if (dao.deleteEmployee(id)) {
                return Response.status(Response.Status.OK).entity("Employee deleted with id " + id).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Employee was not deleted").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting employee").build();
        }
    }
}