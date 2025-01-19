package org.example.service;

import org.example.model.Employee;
import org.example.model.dao.EmployeeDAO;

import javax.jws.WebService;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.example.DbProvider.getDataSource;

/**
 * Standalone-реализация, которая сама устанавливает соединение через DriverManager (H2 in-memory).
 * В реальном проекте лучше использовать пул соединений или DataSource.
 */
@WebService(endpointInterface = "org.example.service.EmployeeService")
public class EmployeeServiceImpl implements EmployeeService {

    Connection conn;

    public EmployeeServiceImpl() {
        try {
            this.conn = getDataSource().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            Statement st = conn.createStatement();
            // Создаём таблицу (если не существует)
            st.execute("CREATE TABLE IF NOT EXISTS employees (" +
                    " id INT PRIMARY KEY," +
                    " first_name VARCHAR(50)," +
                    " last_name VARCHAR(50)," +
                    " position VARCHAR(50)," +
                    " salary DECIMAL(10,2)," +
                    " department VARCHAR(50)" +
                    ");");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Employee> searchEmployees(String firstName,
                                          String lastName,
                                          String position,
                                          Double minSalary,
                                          Double maxSalary,
                                          String department) {
        EmployeeDAO dao = new EmployeeDAO(conn);
        try {
            return dao.searchEmployees(firstName, lastName, position, minSalary, maxSalary, department);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}