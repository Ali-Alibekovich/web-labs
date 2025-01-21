// EmployeeServiceImpl.java
package org.example.service;

import org.example.model.Employee;
import org.example.model.dao.EmployeeDAO;
import org.example.service.errors.EmployeeServiceException;
import org.example.service.errors.ServiceFault;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static org.example.DbProvider.getDataSource;

@WebService(
        endpointInterface = "org.example.service.EmployeeService"
)
public class EmployeeServiceImpl implements EmployeeService {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password";

    @Resource
    private WebServiceContext wsContext;
    private final EmployeeDAO employeeDAO;

    public EmployeeServiceImpl() throws SQLException {
        this.employeeDAO = new EmployeeDAO(getDataSource().getConnection());
    }


    private void authenticate() throws EmployeeServiceException {
        MessageContext msgContext = wsContext.getMessageContext();
        Map<String, List<String>> headers = (Map<String, List<String>>) msgContext.get(MessageContext.HTTP_REQUEST_HEADERS);

        if (headers == null || !headers.containsKey("Authorization")) {
            throw new EmployeeServiceException("Missing Authorization header",
                    ServiceFault.defaultInstance("Unauthorized"));
        }

        String authHeader = headers.get("Authorization").get(0);
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            throw new EmployeeServiceException("Invalid Authorization header",
                    ServiceFault.defaultInstance("Unauthorized"));
        }

        String base64Credentials = authHeader.substring("Basic ".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        String[] values = credentials.split(":", 2);

        if (values.length < 2 || !USERNAME.equals(values[0]) || !PASSWORD.equals(values[1])) {
            throw new EmployeeServiceException("Invalid credentials",
                    ServiceFault.defaultInstance("Unauthorized"));
        }
    }


    @Override
    public List<Employee> searchEmployees(String firstName, String lastName, String position, Double minSalary, Double maxSalary, String department) throws EmployeeServiceException {
        try {
            return employeeDAO.searchEmployees(firstName, lastName, position, minSalary, maxSalary, department);
        } catch (SQLException e) {
            throw new EmployeeServiceException(
                    "Database error during employee search",
                    ServiceFault.defaultInstance(e.getMessage()),
                    e
            );
        }
    }

    @Override
    public int createEmployee(String firstName, String lastName, String position, double salary, String department) throws EmployeeServiceException {
        authenticate();
        if (firstName == null || firstName.isEmpty()) {
            throw new EmployeeServiceException(
                    "Invalid input data",
                    ServiceFault.defaultInstance("First name cannot be null or empty")
            );
        }
        try {
            Employee employee = new Employee(0, firstName, lastName, position, salary, department);
            return employeeDAO.createEmployee(employee);
        } catch (SQLException e) {
            throw new EmployeeServiceException(
                    "Database error during employee creation",
                    ServiceFault.defaultInstance(e.getMessage()),
                    e
            );
        }
    }

    @Override
    public boolean updateEmployee(int id, String firstName, String lastName, String position, double salary, String department) throws EmployeeServiceException {
        authenticate();
        try {
            if (employeeDAO.employeeExists(id)) {
                throw new EmployeeServiceException(
                        "Employee not found",
                        ServiceFault.defaultInstance("No employee exists with ID: " + id)
                );
            }
            Employee employee = new Employee(id, firstName, lastName, position, salary, department);
            return employeeDAO.updateEmployee(id, employee);
        } catch (SQLException e) {
            throw new EmployeeServiceException(
                    "Database error during employee update",
                    ServiceFault.defaultInstance(e.getMessage()),
                    e
            );
        }
    }

    @Override
    public boolean deleteEmployee(int id) throws EmployeeServiceException{
        authenticate();
        try {
            if (employeeDAO.employeeExists(id)) {
                throw new EmployeeServiceException(
                        "Employee not found",
                        ServiceFault.defaultInstance("No employee exists with ID: " + id)
                );
            }
            return employeeDAO.deleteEmployee(id);
        } catch (SQLException e) {
            throw new EmployeeServiceException(
                    "Database error during employee deletion",
                    ServiceFault.defaultInstance(e.getMessage()),
                    e
            );
        }
    }
}
