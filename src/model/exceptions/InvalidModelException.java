package model.exceptions;

public class InvalidModelException extends Exception {
    public InvalidModelException() {
    }

    public InvalidModelException(String message) {
        super(message);
    }

    public InvalidModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidModelException(Throwable cause) {
        super(cause);
    }

    public InvalidModelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}