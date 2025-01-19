package org.example;

import org.example.service.EmployeeServiceImpl;

import javax.xml.ws.Endpoint;

import java.sql.SQLException;

import static org.example.DbProvider.getDataSource;

/**
 * Запускает standalone SOAP-сервис по адресу http://localhost:8080/EmployeeService
 */
public class StandalonePublisher {
    public static void main(String[] args) {
        // Публикуем веб-сервис
        String url = "http://localhost:8080/web/ws/EmployeeService";
        try {
            Endpoint.publish(url, new EmployeeServiceImpl());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("SOAP-сервис запущен по адресу: " + url + "?wsdl");
        System.out.println("Не останавливается автоматически. Нажмите Ctrl+C для завершения.");
    }
}