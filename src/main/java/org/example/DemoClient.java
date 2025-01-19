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
        // Адрес WSDL того же сервиса, который мы опубликовали
            URL wsdlURL = new URL("http://localhost:8080/web/ws/EmployeeService?wsdl");

        // Параметры QName: targetNamespace (определяется исходя из пакета класса EmployeeServiceImpl)
        // и имя сервиса (по умолчанию "<имя_класса>Service", если явно не указано @WebService(serviceName="...").
        //
        // В нашем случае targetNamespace будет "http://service.example.com/"
        // -- он формируется из пакета "com.example.service" в обратном порядке: com.example.service -> http://service.example.com/
        //
        // Название сервиса по умолчанию: "EmployeeServiceImplService".
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