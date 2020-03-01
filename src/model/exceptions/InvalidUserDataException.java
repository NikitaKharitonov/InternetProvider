package model.exceptions;

public class InvalidUserDataException extends IllegalArgumentException {
    public InvalidUserDataException() {
    }

    public InvalidUserDataException(String s) {
        super(s);
    }

    public InvalidUserDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidUserDataException(Throwable cause) {
        super(cause);
    }
}
