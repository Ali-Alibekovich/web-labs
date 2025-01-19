package org.example.service.errors;

public class ServiceFault {
    private String message;

    public ServiceFault() {}

    public ServiceFault(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ServiceFault defaultInstance(String message) {
        return new ServiceFault(message);
    }
}