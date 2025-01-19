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
}