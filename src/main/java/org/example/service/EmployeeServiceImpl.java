// EmployeeServiceImpl.java
package org.example.service;

import org.example.model.Employee;
import org.example.model.dao.EmployeeDAO;

import javax.jws.WebService;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.example.DbProvider.getDataSource;

@WebService(endpointInterface = "org.example.service.EmployeeService")
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDAO employeeDAO;

    public EmployeeServiceImpl() throws SQLException {
        this.employeeDAO = new EmployeeDAO(getDataSource().getConnection());
    }

    @Override
    public List<Employee> searchEmployees(String firstName, String lastName, String position, Double minSalary, Double maxSalary, String department) {
        try {
            return employeeDAO.searchEmployees(firstName, lastName, position, minSalary, maxSalary, department);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int createEmployee(String firstName, String lastName, String position, double salary, String department) {
        try {
            Employee employee = new Employee(0, firstName, lastName, position, salary, department);
            return employeeDAO.createEmployee(employee);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean updateEmployee(int id, String firstName, String lastName, String position, double salary, String department) {
        try {
            Employee employee = new Employee(id, firstName, lastName, position, salary, department);
            return employeeDAO.updateEmployee(id, employee);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteEmployee(int id) {
        try {
            return employeeDAO.deleteEmployee(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
