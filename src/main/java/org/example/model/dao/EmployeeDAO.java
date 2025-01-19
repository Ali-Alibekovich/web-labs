package org.example.model.dao;

import org.example.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    private final Connection connection;

    public EmployeeDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Поиск сотрудников по заданным критериям.
     * Любой из аргументов может быть null (или пустым для строк),
     * тогда соответствующее условие не применяется.
     */
    public List<Employee> searchEmployees(String firstName,
                                          String lastName,
                                          String position,
                                          Double minSalary,
                                          Double maxSalary,
                                          String department) throws SQLException {

        StringBuilder sql = new StringBuilder("SELECT id, first_name, last_name, position, salary, department FROM employees WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (firstName != null && !firstName.isEmpty()) {
            sql.append(" AND first_name = ?");
            params.add(firstName);
        }
        if (lastName != null && !lastName.isEmpty()) {
            sql.append(" AND last_name = ?");
            params.add(lastName);
        }
        if (position != null && !position.isEmpty()) {
            sql.append(" AND position = ?");
            params.add(position);
        }
        if (minSalary != null) {
            sql.append(" AND salary >= ?");
            params.add(minSalary);
        }
        if (maxSalary != null) {
            sql.append(" AND salary <= ?");
            params.add(maxSalary);
        }
        if (department != null && !department.isEmpty()) {
            sql.append(" AND department = ?");
            params.add(department);
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                List<Employee> result = new ArrayList<>();
                while (rs.next()) {
                    Employee e = new Employee(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("position"),
                            rs.getDouble("salary"),
                            rs.getString("department")
                    );
                    result.add(e);
                }
                return result;
            }
        }
    }

    /**
     * Создание новой записи.
     */
    public int createEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO employees (first_name, last_name, position, salary, department) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            ps.setString(3, employee.getPosition());
            ps.setDouble(4, employee.getSalary());
            ps.setString(5, employee.getDepartment());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve generated ID.");
                }
            }
        }
    }

    /**
     * Обновление существующей записи.
     */
    public boolean updateEmployee(int id, Employee employee) throws SQLException {
        String sql = "UPDATE employees SET first_name = ?, last_name = ?, position = ?, salary = ?, department = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            ps.setString(3, employee.getPosition());
            ps.setDouble(4, employee.getSalary());
            ps.setString(5, employee.getDepartment());
            ps.setInt(6, id);

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        }
    }

    /**
     * Удаление записи по ID.
     */
    public boolean deleteEmployee(int id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;
        }
    }
}