package org.example;

import org.example.model.Employee;
import org.example.service.EmployeeService;
import org.example.service.EmployeeServiceImpl;
import org.example.service.errors.EmployeeServiceException;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;
import java.util.List;

/**
 * Демонстрационный клиент, который:
 * 1) Пытается подключиться к уже запущенному сервису по WSDL.
 * 2) Вызывает метод searchEmployees.
 * Для работы нужно, чтобы StandalonePublisher уже был запущен.
 */
public class DemoClient {
    public static void main(String[] args) throws Exception {
        URL wsdlURL = new URL("http://localhost:8080/web/ws/EmployeeService?wsdl");

        QName qname = new QName("http://service.example.org/", "EmployeeServiceImplService");

        // Создаём сервис JAX-WS
        Service service = Service.create(wsdlURL, qname);

        // Получаем порт (прокси), который реализует интерфейс EmployeeService
        EmployeeService port = service.getPort(EmployeeService.class);

        try {
            int newId = port.createEmployee("", "Doe", "Developer", 5000, "IT");
            System.out.println("New employee ID: " + newId);
        } catch (EmployeeServiceException e) {
            System.out.println("Error during employee creation: " + e.getFaultInfo().getMessage());
        }

        // Пример вызова метода удаления с ошибкой
        try {
            boolean deleted = port.deleteEmployee(999); // Неверный ID
            System.out.println("Employee deleted: " + deleted);
        } catch (EmployeeServiceException e) {
            System.out.println("Error during employee deletion: " + e.getFaultInfo().getMessage());
        }

    }
}