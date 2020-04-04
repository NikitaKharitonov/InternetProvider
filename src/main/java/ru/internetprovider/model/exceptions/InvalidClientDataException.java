package ru.internetprovider.model.exceptions;

public class InvalidClientDataException extends IllegalArgumentException {
    public InvalidClientDataException() {
    }

    public InvalidClientDataException(String s) {
        super(s);
    }

    public InvalidClientDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidClientDataException(Throwable cause) {
        super(cause);
    }
}
