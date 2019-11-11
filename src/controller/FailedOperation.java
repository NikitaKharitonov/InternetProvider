package controller;

class FailedOperation extends Exception{
    FailedOperation(String message) {
        super(message);
    }
}
