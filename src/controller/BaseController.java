package controller;

import model.Model;
import model.User;
import model.services.Internet;
import model.services.Phone;
import model.services.Service;
import model.services.Television;

import java.io.IOException;
import java.util.ArrayList;


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
    public User getUser(int userID){
        return model.getUserById(userID);
    }

    @Override
    public String[] getProvidedServices() {
        return SERVICES_NAME;
    }

    @Override
    public Service getService(String serviceType, int serviceID) throws FailedOperation {

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
    public ArrayList<? extends Service> getAllServices(String serviceType) throws FailedOperation {
        int count;
        ArrayList<? extends Service> result;
        switch (serviceType) {
            case "Internets":
                result = model.getInternets();
                break;
            case "Televisions":
                result = model.getTelevisions();
                break;
            case "Phones":
                result = model.getPhones();
                break;
            default:
                throw new FailedOperation("Unexpected service name: " + serviceType);
        }
        return result;
    }

    @Override
    public void setServiceToUser(int userID, Service service) throws FailedOperation {

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

        model.deleteUserById(userID);
    }

    @Override
    public void deleteService(String serviceType, int serviceID) throws FailedOperation {

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
}
