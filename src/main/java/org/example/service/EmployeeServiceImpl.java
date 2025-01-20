package org.example.service;

import org.example.model.Employee;
import org.example.model.dao.EmployeeDAO;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.example.DbProvider.getDataSource;


@Path("/employee")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeServiceImpl implements EmployeeService {

    Connection conn;

    EmployeeDAO dao;

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

    @Override
    public List<Employee> searchEmployees(String firstName,
                                          String lastName,
                                          String position,
                                          Double minSalary,
                                          Double maxSalary,
                                          String department) {

        try {
            return dao.searchEmployees(firstName, lastName, position, minSalary, maxSalary, department);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int createEmployee(String firstName, String lastName, String position, Double salary, String department) {
        try {
            Employee employee = new Employee(0, firstName, lastName, position, salary, department);
            return dao.createEmployee(employee);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean updateEmployee(int id, String firstName, String lastName, String position, Double salary, String department) {
        try {
            Employee employee = new Employee(id, firstName, lastName, position, salary, department);
            return dao.updateEmployee(id, employee);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteEmployee(int id) {
        try {
            return dao.deleteEmployee(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}