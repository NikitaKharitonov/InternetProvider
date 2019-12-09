package controller;

import model.*;
import model.exceptions.ServiceNotFoundException;
import model.exceptions.UserNotFoundException;
import model.services.Internet;
import model.services.Service;
import model.users.User;

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
            throw new FailedOperation(ex.getMessage());
        }
    }

    @Override
    public void createService(Service service) throws FailedOperation {
        model.addService(service);
        try{
            model.save();
        }
        catch (IOException ex){
            throw new FailedOperation(ex.getMessage());
        }
    }

    @Override
    public User getUser(int userID) throws UserNotFoundException {
        return model.getUserById(userID);
    }

    @Override
    public String[] getProvidedServices() {
        return SERVICES_NAME;
    }

    @Override
    public Service getService(long serviceID) throws FailedOperation {
        try {
            return model.getServiceById(serviceID);
        } catch (ServiceNotFoundException ex) {
            throw new FailedOperation(ex.getMessage());
        }
    }

    @Override
    public ArrayList<Service> getAllServices(String serviceType) throws ServiceNotFoundException{
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
    public void setServiceToUser(long userID, long serviceId) throws FailedOperation {
        try{
            model.setServiceToUser(userID, serviceId);
            model.save();
        }
        catch (IOException | UserNotFoundException | ServiceNotFoundException ex){
            throw new FailedOperation(ex.getMessage());
        }
    }

    @Override
    public void changeUserData(User user) throws FailedOperation {
        try{
            model.removeUserById(user.getId());
            model.addUser(user);
            model.save();
        }
        catch (IOException | UserNotFoundException ex){
            throw new FailedOperation(ex.getMessage());
        }
    }

    @Override
    public void changeService(Service service) throws FailedOperation {
        try{
            model.removeServiceById(service.getId());
            model.addService(service);
            model.save();
        }
        catch (IOException | ServiceNotFoundException ex){
            throw new FailedOperation(ex.getMessage());
        }
    }

    @Override
    public void removeServiceFromUser(long userID, String serviceType) throws FailedOperation {
        try{
            User user = model.getUserById(userID);
            user.removeServiceByType(serviceType);
            model.save();
        }
        catch (IOException | UserNotFoundException ex){
            throw new FailedOperation(ex.getMessage());
        }
    }

    @Override
    public void deleteUser(long userID) throws FailedOperation {
        try {
            model.removeUserById(userID);
        } catch (UserNotFoundException ex) {
            throw new FailedOperation(ex.getMessage());
        }
    }

    @Override
    public void deleteService(long serviceID) throws FailedOperation {
        try{
            model.removeServiceById(serviceID);
            model.save();
        }
        catch (IOException | ServiceNotFoundException ex){
            throw new FailedOperation(ex.getMessage());
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
        return Internet.ConnectionType.valueOf(connectionType);
    }
}
