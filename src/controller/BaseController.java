package controller;

import model.Model;
import model.User;
import model.services.Internet;
import model.services.Phone;
import model.services.Service;
import model.services.Television;

import java.io.IOException;


public class BaseController implements Controller {

    private Model model;
    private static final String[] SERVICES_NAME = { "Internet", "Phone", "Television" };

    public BaseController(Model model){
        this.model = model;
    }

    @Override
    public void createUser(User user) throws FailedOperation {
        model.addUser(user);
        try{
            model.save();
        }
        catch (IOException ex){
            throw new FailedOperation("Some issues with model were happened");
        }
    }

    @Override
    public void createService(Service service) throws FailedOperation {
        switch (service.getType()){
            case "Internet":
                model.addInternet((Internet)service);
                break;
            case "Television":
                model.addTelevision((Television)service);
                break;
            case "Phone":
                model.addPhone((Phone)service);
                break;
            default:
                throw new FailedOperation("Unsupported service : " + service.getType());
        }

        try{
            model.save();
        }
        catch (IOException ex){
            throw new FailedOperation("Some issues with model were happened");
        }
    }

    @Override
    public User getUser(int userID) throws FailedOperation {
        if (!isValidID("User", userID)){
            throw new FailedOperation("Incorrect user ID");
        }
        return model.getUserById(userID);
    }

    @Override
    public String[] getProvidedServices() {
        return SERVICES_NAME;
    }

    @Override
    public Service getService(String serviceType, int serviceID) throws FailedOperation {
        if (!isValidID(serviceType, serviceID)){
            throw new FailedOperation("Incorrect user ID");
        }
        switch (serviceType){
            case "Internet":
                return model.getInternetById(serviceID);
            case "Television":
                return model.getTelevisionById(serviceID);
            case "Phone":
                return model.getPhoneById(serviceID);
            default:
                throw new FailedOperation("Unsupported service : " + serviceType);
        }
    }

    @Override
    public Service[] getAllServices(String serviceType) throws FailedOperation {
        int count;
        Service[] result;
        switch (serviceType) {
            case "Internets":
                count = model.getInternetCount();
                result = new Service[count];
                for (int i = 0; i < count; i++) {
                    result[i] = model.getInternetById(i);
                }
                break;
            case "Televisions":
                count = model.getTelevisionCount();
                result = new Service[count];
                for (int i = 0; i < count; i++) {
                    result[i] = model.getTelevisionById(i);
                }
                break;
            case "Phones":
                count = model.getPhoneCount();
                result = new Service[count];
                for (int i = 0; i < count; i++) {
                    result[i] = model.getPhoneById(i);
                }
                break;
            default:
                throw new FailedOperation("Unexpected service name: " + serviceType);
        }
        return result;
    }

    @Override
    public void setServiceToUser(int userID, Service service) throws FailedOperation {
        if (!isValidID("User", userID)){
            throw new FailedOperation("Incorrect user ID");
        }
        User user = model.getUserById(userID);
        switch (service.getType()) {
            case "Internets":
                user.setInternet((Internet)service);
                break;
            case "Televisions":
                user.setTelevision((Television)service);
                break;
            case "Phones":
                user.setPhone((Phone)service);
                break;
            default:
                throw new FailedOperation("Unexpected service name: " + service.getType());
        }
        try{
            model.save();
        }
        catch (IOException ex){
            throw new FailedOperation("Some issues with model were happened");
        }
    }

    @Override
    public void changeUserData(int userID, User user) throws FailedOperation {
        if (!isValidID("User", userID)){
            throw new FailedOperation("Incorrect user ID");
        }
        model.setUserById(userID, user);
        try{
            model.save();
        }
        catch (IOException ex){
            throw new FailedOperation("Some issues with model were happened");
        }
    }

    @Override
    public void changeService(int serviceID, Service service) throws FailedOperation {
        if (!isValidID(service.getType(), serviceID)){
            throw new FailedOperation("Incorrect user ID");
        }

        switch (service.getType()) {
            case "Internets":
                model.setInternetById(serviceID, (Internet)service);
                break;
            case "Televisions":
                model.setTelevisionById(serviceID, (Television)service);
                break;
            case "Phones":
                model.setPhoneById(serviceID, (Phone)service);
                break;
            default:
                throw new FailedOperation("Unexpected service name: " + service.getType());
        }
        try{
            model.save();
        }
        catch (IOException ex){
            throw new FailedOperation("Some issues with model were happened");
        }
    }

    @Override
    public void removeServiceFromUser(int userID, String serviceType) throws FailedOperation {
        if (!isValidID("User", userID)){
            throw new FailedOperation("Incorrect user ID");
        }
        User user = model.getUserById(userID);
        switch (serviceType) {
            case "Internets":
                user.setInternet(null);
                break;
            case "Televisions":
                user.setTelevision(null);
                break;
            case "Phones":
                user.setPhone(null);
                break;
            default:
                throw new FailedOperation("Unexpected service name: " + serviceType);
        }
        try{
            model.save();
        }
        catch (IOException ex){
            throw new FailedOperation("Some issues with model were happened");
        }
    }

    @Override
    public void deleteUser(int userID) throws FailedOperation {
        if (!isValidID("User", userID)){
            throw new FailedOperation("Incorrect user ID");
        }
        model.deleteUserById(userID);
    }

    @Override
    public void deleteService(String serviceType, int serviceID) throws FailedOperation {
        if (!isValidID(serviceType, serviceID)){
            throw new FailedOperation("Incorrect user ID");
        }
        switch (serviceType) {
            case "Internets":
                model.deleteInternetById(serviceID);
                break;
            case "Televisions":
                model.deleteTelevisionById(serviceID);
                break;
            case "Phones":
                model.deletePhoneById(serviceID);
                break;
            default:
                throw new FailedOperation("Unexpected service name: " + serviceType);
        }
        try{
            model.save();
        }
        catch (IOException ex){
            throw new FailedOperation("Some issues with model were happened");
        }
    }
    private boolean isValidID(String type, int ID) {
        int count;
        switch (type){
            case "User":
                count = model.getUserCount();
                break;
            case "Internet":
                count = model.getInternetCount();
                break;
            case "Television":
                count = model.getTelevisionCount();
                break;
            case "Phone":
                count = model.getPhoneCount();
                break;
            default:
                return false;
        }
        return (count >= ID || ID < 0);
    }
}
