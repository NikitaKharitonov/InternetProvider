package ru.internetprovider.model.exceptions;

public class ServiceNotFoundException extends Exception {
    public ServiceNotFoundException(String message) {
        super(message);
    }

    public ServiceNotFoundException() {

    }
}
