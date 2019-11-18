package controller;
/**
 * Class FailedOperation extends Exception
 * @author anteii
 * @version 0.1
 * */
public class FailedOperation extends Exception{
    FailedOperation(String message) {
        super(message);
    }
}
