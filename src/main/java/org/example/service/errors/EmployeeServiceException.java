package org.example.service.errors;

import javax.xml.ws.WebFault;

@WebFault(faultBean = "org.example.service.errors.ServiceFault")
public class EmployeeServiceException extends Exception {
    private static final long serialVersionUID = 1L;
    private final ServiceFault fault;

    public EmployeeServiceException(String message, ServiceFault fault) {
        super(message);
        this.fault = fault;
    }

    public EmployeeServiceException(String message, ServiceFault fault, Throwable cause) {
        super(message, cause);
        this.fault = fault;
    }

    public ServiceFault getFaultInfo() {
        return fault;
    }
}