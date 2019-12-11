package server.controller;
/**
 * Class FailedOperation extends Exception
 * @author anteii
 * @version 0.1
 * */
public class FailedOperation extends Exception{
    public FailedOperation(Throwable cause) {
        super(cause);
    }

    FailedOperation(String message) {
        super(message);
    }
}
