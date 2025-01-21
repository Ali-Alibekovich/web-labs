package org.example;

import org.example.service.EmployeeService;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPBinding;
import java.net.URL;
import java.util.*;

public class DemoClient {

    public static void main(String[] args) throws Exception {
        URL wsdlURL = new URL("http://localhost:8080/web/ws/EmployeeService?wsdl");
        QName qname = new QName("http://service.example.org/", "EmployeeServiceImplService");

        // Создание сервиса
        Service service = Service.create(wsdlURL, qname);

        // Получение порта
        EmployeeService port = service.getPort(EmployeeService.class);

        // Настройка обработчика
        SOAPBinding binding = (SOAPBinding) ((BindingProvider) port).getBinding();
        List<Handler> handlerChain = new ArrayList<>();
        handlerChain.add(new AuthorizationHandler());
        binding.setHandlerChain(handlerChain);

        // Вызов метода
        int newId = port.createEmployee("John", "Doe", "Developer", 5000, "IT");
        System.out.println("New employee ID: " + newId);
    }
}