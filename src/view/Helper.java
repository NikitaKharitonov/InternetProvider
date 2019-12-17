package view;

import controller.Controller;
import controller.FailedOperation;
import model.exceptions.ServiceNotFoundException;
import model.users.User;
import model.services.Internet;
import model.services.Phone;
import model.services.Service;
import model.services.Television;

import java.io.*;
import java.util.HashMap;

public class Helper {
    private Controller controller;
    private BufferedWriter outputStream;

    public Helper(Controller controller, OutputStream outputStream) {
        this.outputStream = new BufferedWriter( new OutputStreamWriter(outputStream));
        this.controller = controller;
    }

    public User getUser(int userID) throws FailedOperation {
        return controller.getUser(userID);
    }

    public void printUser(int userID) throws FailedOperation {
        User user = getUser(userID);
        println(user.toString());
    }

    public void printUsers() {
        if (controller.getUsersData().equals("")) {
            println("No users.");
        } else {
            println(controller.getUsersData());
        }
    }

    public Service getService(long serviceID) throws FailedOperation {
        return controller.getService(serviceID);
    }

    public void printService(long serviceID) throws FailedOperation {
        Service service = getService(serviceID);
        switch (service.getType()) {
            case "Internet":
                println(((Internet) service).toString());
                break;
            case "Television":
                println(((Television) service).toString());
                break;
            case "Phone":
                println(((Phone) service).toString());
                break;
        }
    }

    public void printServices() {
        if (controller.getServicesData().equals("")) {
            println("No services");
        } else {
            println(controller.getServicesData());
        }
    }

    public void printUserService(int userID, String serviceType)
            throws FailedOperation {
        User user = getUser(userID);
        try {
            printService(user.getServiceIdByType(serviceType));
        } catch (ServiceNotFoundException e){
            throw new FailedOperation(e);
        }
    }

    public void createUser(String name, String phoneNumber, String emailAddress) throws FailedOperation {
        controller.createUser(new User(controller.getNextUserId(), name, phoneNumber, emailAddress));
        println("User was created.");
    }

    public void createInternet(String name, int speed, boolean antivirus, String connectionType)
            throws FailedOperation {
        controller.createService(new Internet(controller.getNextServiceId(), name, speed, antivirus,
                controller.getConnectionType(connectionType)));
        println("Internet was created.");
    }

    public void createPhone(String name, int callsMinCount, int smsCount) throws FailedOperation {
        controller.createService(new Phone(controller.getNextServiceId(), name, callsMinCount, smsCount));
        println("Phone was created.");
    }

    public void createTelevision(String name, int numberOfChannels) throws FailedOperation {
        controller.createService(new Television(controller.getNextServiceId(), name, numberOfChannels));
        println("Television was created.");
    }

    public void changeUser(int userID, HashMap<String, String> params) throws FailedOperation {
        User user = getUser(userID);
        if (params.containsKey("name")) {
            user.setName(params.get("name"));
        }
        if (params.containsKey("phone")) {
            user.setPhoneNumber(params.get("phone"));
        }
        if (params.containsKey("email")) {
            user.setEmailAddress(params.get("email"));
        }
        controller.changeUserData(user);
        println("User has been changed.");
    }

    public void changeInternet(int serviceID, HashMap<String, String> params)
            throws FailedOperation {
        Internet internet;
        try {
            internet = (Internet) getService(serviceID);
        } catch (ClassCastException e) {
            throw new FailedOperation(new ServiceNotFoundException("User with id " + serviceID + " not found"));
        }
        if (params.containsKey("name")) {
            internet.setName(params.get("name"));
        }
        if (params.containsKey("speed")) {
            internet.setSpeed(Integer.parseInt(params.get("speed")));
        }
        if (params.containsKey("antivirus")) {
            internet.setAntivirus(Boolean.parseBoolean(params.get("antivirus")));
        }
        if (params.containsKey("connectionType")) {
            internet.setConnectionType(controller.getConnectionType(params.get("connectionType")));
        }
        controller.createService(internet);
        println("Internet has been changed");
    }

    public void changePhone(int serviceID, HashMap<String, String> params)
            throws FailedOperation {
        Phone phone;
        try {
            phone = (Phone) getService(serviceID);
        } catch (ClassCastException e) {
            throw new FailedOperation(new ServiceNotFoundException("User with id " + serviceID + " not found"));
        }
        if (params.containsKey("name")) {
            phone.setName(params.get("name"));
        }
        if (params.containsKey("callsMinCount")) {
            phone.setCallsMinCount(Integer.parseInt(params.get("callsMinCount")));
        }
        if (params.containsKey("smsCount")) {
            phone.setSmsCount(Integer.parseInt(params.get("smsCount")));
        }
        controller.createService(phone);
        println("Phone has been changed.");
    }

    public void changeTelevision(int serviceID, HashMap<String, String> params)
            throws FailedOperation {
        Television television;
        try {
            television = (Television) getService(serviceID);
        } catch (ClassCastException e) {
            throw new FailedOperation(new ServiceNotFoundException("User with id " + serviceID + " not found"));
        }
        if (params.containsKey("name")) {
            television.setName(params.get("name"));
        }
        if (params.containsKey("numberOfChannels")) {
            television.setNumberOfChannels(Integer.parseInt(params.get("numberOfChannels")));
        }
        controller.createService(television);
        println("Television has been changed.");
    }

    public void setUserService(int userID, long serviceID)
            throws FailedOperation {
        controller.setServiceToUser(userID, serviceID);
        println("User service has been changed.");
    }

    public void deleteUser(int userID) throws FailedOperation {
        controller.deleteUser(userID);
        println("User has been deleted.");
    }

    public void deleteService(int serviceID) throws FailedOperation {
        controller.deleteService(serviceID);
        println("Service has been deleted.");
    }

    public void deleteUserService(int userID, String serviceType) throws FailedOperation {
        controller.removeServiceFromUser(userID, serviceType);
    }

    private void println(String msg){
        try {
            outputStream.write(msg);
            outputStream.newLine();
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
