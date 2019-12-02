package controller;

import model.IdGenerator;
import model.Model;
import model.User;
import model.services.Internet;
import model.services.Phone;
import model.services.Service;
import model.services.Television;

import java.io.IOException;
import java.util.ArrayList;


public class BaseController implements Controller {

    private IdGenerator serviceIdGenerator;
    private IdGenerator userIdGenerator;
    private Model model;
    private static final String[] SERVICES_NAME = { "Internet", "Phone", "Television" };

    public BaseController(Model model){
        this.model = model;
        serviceIdGenerator = new IdGenerator(model.getServiceMaxId());
        userIdGenerator = new IdGenerator(model.getUserMaxId());
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
        model.addService(service);

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
    public Service getService(long serviceID){
        return model.getServiceById(serviceID);
    }

    @Override
    public ArrayList<Service> getAllServices(String serviceType){
        ArrayList<Service> result = new ArrayList<>();
            Service temp;
            for (long i = 1; i <= model.getServiceCount(); i++) {
                temp = model.getServiceById(i);
                if (temp.getType().equals(serviceType)){
                    result.add(temp);
                }
        }
        return result;
    }

    @Override
    public void setServiceToUser(int userID, Service service) throws FailedOperation {

        User user = model.getUserById(userID);
        user.putService(service);

        try{
            model.save();
        }
        catch (IOException ex){
            throw new FailedOperation("Some issues with model were happened");
        }
    }

    @Override
    public void changeUserData(User user) throws FailedOperation {
        model.removeUserById(user.getId());
        model.addUser(user);
        try{
            model.save();
        }
        catch (IOException ex){
            throw new FailedOperation("Some issues with model were happened");
        }
    }

    @Override
    public void changeService(Service service) throws FailedOperation {

        model.removeServiceById(service.getId());
        model.addService(service);
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
        user.getServiceMap().remove(serviceType);
        try{
            model.save();
        }
        catch (IOException ex){
            throw new FailedOperation("Some issues with model were happened");
        }
    }

    @Override
    public void deleteUser(int userID) throws FailedOperation {
        model.removeUserById(userID);
    }

    @Override
    public void deleteService(long serviceID) throws FailedOperation {

        model.removeServiceById(serviceID);
        try{
            model.save();
        }
        catch (IOException ex){
            throw new FailedOperation("Some issues with model were happened");
        }
    }

    @Override
    public long getNextServiceId() {
        return serviceIdGenerator.next();
    }
    @Override
    public long getNextUserId() {
        return userIdGenerator.next();
    }

    @Override
    public Internet.ConnectionType getConnectionType(String connectionType) {
        switch (connectionType) {
            case "ADSL":
                return Internet.ConnectionType.ADSL;
            case "DialUp":
                return Internet.ConnectionType.Dial_up;
            case "ISDN":
                return Internet.ConnectionType.ISDN;
            case "Cable":
                return Internet.ConnectionType.Cable;
            default:
                return Internet.ConnectionType.Fiber;
        }
    }
}
