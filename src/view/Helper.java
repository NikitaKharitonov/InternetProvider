package view;

import controller.Controller;
import controller.FailedOperation;
import model.ServiceNotFoundException;
import model.User;
import model.UserNotFoundException;
import model.services.Internet;
import model.services.Phone;
import model.services.Service;
import model.services.Television;

import java.util.ArrayList;
import java.util.HashMap;

public class Helper {
    private Controller controller;

    public Helper(Controller controller) {
        this.controller = controller;
    }

    public User getUser(int userID) throws UserNotFoundException, FailedOperation {
        return controller.getUser(userID);
    }

    public void printUser(int userID) throws FailedOperation, UserNotFoundException {
        User user = getUser(userID);
        System.out.println("Id: " + user.getId());
        System.out.println("Name: " + user.getName());
        System.out.println("Phone number: " + user.getPhoneNumber());
        System.out.println("Email: " + user.getEmailAddress());
    }

    public Service getService(long serviceID) throws FailedOperation, ServiceNotFoundException {
        return controller.getService(serviceID);
    }

    public void printService(long serviceID) throws FailedOperation, ServiceNotFoundException {
        Service service = getService(serviceID);
        System.out.println("Id: " + service.getId());
        System.out.println("Type: " + service.getType());
        System.out.println("Name: " + service.getName());
        switch (service.getType()) {
            case "Internet":
                System.out.println(((Internet) service).toString());
                break;
            case "Television":
                System.out.println(((Television) service).toString());
                break;
            case "Phone":
                System.out.println(((Phone) service).toString());
                break;
        }
    }

    public void printServices(String serviceType) throws FailedOperation, ServiceNotFoundException {
        ArrayList<Service> services = controller.getAllServices(serviceType);
        if (!services.isEmpty()) {
            for (Service curService : services) {
                System.out.print("Id: " + curService.getId() + " ");
                System.out.println(curService.toString());
            }
        } else {
            throw new ServiceNotFoundException("");
        }
    }

    public void printUserService(int userID, String serviceType)
            throws FailedOperation, UserNotFoundException, ServiceNotFoundException {
        User user = getUser(userID);
        printService(user.getServiceIdByType(serviceType));
    }

    public void createUser(String name, String phoneNumber, String emailAddress) throws FailedOperation {
        controller.createUser(new User(controller.getNextUserId(), name, phoneNumber, emailAddress));
        System.out.println("User was created.");
    }

    public void createInternet(String name, int speed, boolean antivirus, String connectionType)
            throws FailedOperation {
        controller.createService(new Internet(controller.getNextServiceId(), name, speed, antivirus,
                controller.getConnectionType(connectionType)));
        System.out.println("Internet was created.");
    }

    public void createPhone(String name, int callsMinCount, int smsCount) throws FailedOperation {
        controller.createService(new Phone(controller.getNextServiceId(), name, callsMinCount, smsCount));
        System.out.println("Phone was created.");
    }

    public void createTelevision(String name, int numberOfChannels) throws FailedOperation {
        controller.createService(new Television(controller.getNextServiceId(), name, numberOfChannels));
        System.out.println("Television was created.");
    }

    public void changeUser(int userID, HashMap<String, String> params) throws FailedOperation, UserNotFoundException {
        User user = getUser(userID);
        if (params.containsKey("name")) {
            user.setName(params.get("name"));
            System.out.println("User name has been changed.");
        }
        if (params.containsKey("phone")) {
            user.setPhoneNumber(params.get("phone"));
            System.out.println("User phone number has been changed.");
        }
        if (params.containsKey("email")) {
            user.setEmailAddress(params.get("email"));
            System.out.println("User email address has been changed.");
        }
        controller.changeUserData(user);
    }
}
