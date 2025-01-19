package org.example;

import org.example.model.Employee;
import org.example.service.EmployeeService;
import org.example.service.EmployeeServiceImpl;

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

        // все сотрудники
        List<Employee> employeesExists = port.searchEmployees(null, null, null, null, null, null);
        System.out.println("Найдено сотрудников после операции create: " + employeesExists.size());
        for (Employee e : employeesExists) {
            System.out.println(
                    "ID: " + e.getId() +
                            ", " + e.getFirstName() + " " + e.getLastName() +
                            ", " + e.getPosition() +
                            ", salary=" + e.getSalary() +
                            ", dept=" + e.getDepartment()
            );
        }

        // создаем запись
        port.createEmployee("Ali", "Chupanov", "DEVELOPER", 100000.0, "IT");
        List<Employee> employees = port.searchEmployees("Ali", "Chupanov", "DEVELOPER", null,null, "IT");
        System.out.println("Найдено сотрудников после операции create: " + employees.size());
        for (Employee e : employees) {
            System.out.println(
                    "ID: " + e.getId() +
                            ", " + e.getFirstName() + " " + e.getLastName() +
                            ", " + e.getPosition() +
                            ", salary=" + e.getSalary() +
                            ", dept=" + e.getDepartment()
            );
        }

        //удаляем запись
        port.updateEmployee(employees.get(0).getId(), "Ali", "Chupanov", "DEVELOPER", 120000.0, "IT");
        List<Employee> employeesAfterUpdate = port.searchEmployees("Ali", "Chupanov", "DEVELOPER", null,null, "IT");
        System.out.println("Найдено сотрудников после операции update: " + employees.size());
        for (Employee e : employeesAfterUpdate) {
            System.out.println(
                    "ID: " + e.getId() +
                            ", " + e.getFirstName() + " " + e.getLastName() +
                            ", " + e.getPosition() +
                            ", salary=" + e.getSalary() +
                            ", dept=" + e.getDepartment()
            );
        }


        //удаляем запись
        port.deleteEmployee(employees.get(0).getId());
        int size =
                port.searchEmployees("Ali", "Chupanov", "DEVELOPER", null,null, "IT").size();
        //должно быть 0
        System.out.println("Найдено сотрудников после операции delete: " + size);



    }
}