package org.example;

import org.example.model.Employee;
import org.example.service.EmployeeService;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;
import java.util.List;

public class DemoClient {
    public static void main(String[] args) throws Exception {
        URL wsdlURL = new URL("http://localhost:8080/web/ws/EmployeeService?wsdl");

        QName qname = new QName("http://service.example.org/", "EmployeeServiceImplService");

        // Создаём сервис JAX-WS
        Service service = Service.create(wsdlURL, qname);

        // Получаем порт (прокси), который реализует интерфейс EmployeeService
        EmployeeService port = service.getPort(EmployeeService.class);

        // Вызываем метод
        // Пример: хотим найти всех Developers (position="Developer"), в отделе IT (department="IT"),
        // без ограничений по зарплате
        List<Employee> employees = port.searchEmployees(null, null, "Developer", null, null, "IT");

        System.out.println("Найдено сотрудников: " + employees.size());
        for (Employee e : employees) {
            System.out.println(
                    "ID: " + e.getId() +
                            ", " + e.getFirstName() + " " + e.getLastName() +
                            ", " + e.getPosition() +
                            ", salary=" + e.getSalary() +
                            ", dept=" + e.getDepartment()
            );
        }
    }
}