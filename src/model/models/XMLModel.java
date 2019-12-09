package model.models;

import model.Model;
import model.exceptions.ServiceNotFoundException;
import model.exceptions.UserNotFoundException;
import model.services.Service;
import model.users.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class XMLModel implements Model {
    private HashMap<Long, User> users; // each key represents the user's ID

    private HashMap<Long, Service> services; // each key represents the service's ID

    private final String projectDir = System.getProperty("user.dir");
    private final String usersDataPath = projectDir + "/data/xml/users"; // file for the services data storage
    private final String serviceDataPath = projectDir + "/data/xml/services"; // file for the services data storage

    public XMLModel() throws IOException {
        services = Models.readServicesFromXML(serviceDataPath);
        users = Models.readUsersFromXML(usersDataPath);
    }

    public void save() throws IOException {
        Models.writeServicesToXML(services, serviceDataPath);
        Models.writeUsersToXML(users, usersDataPath);
    }

    @Override
    public User getUserById(long id) throws UserNotFoundException {
        User user = users.get(id);
        if(user == null)
            throw new UserNotFoundException("User with id " + id + " not found");
        return user;
    }

    @Override
    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void removeUserById(long id) {
        users.remove(id);
    }

    @Override
    public int getUserCount() {
        return users.size();
    }

    @Override
    public long getUserMaxId() {
        Set keys = users.keySet();
        if (keys.size() == 0)
            return 0;
        else {
            Long[] ids = new Long[keys.size()];
            keys.toArray(ids);
            Arrays.sort(ids);
            return ids[ids.length - 1];
        }
    }

    public String getUserData() {
        return Models.usersToString(users);
    }

    @Override
    public Service getServiceById(long id) throws ServiceNotFoundException {
        Service service = services.get(id);
        if (service == null)
            throw new ServiceNotFoundException("Service with id " + id + " not found");
        return service;
    }

    @Override
    public void addService(Service service) {
        services.put(service.getId(), service);
    }

    @Override
    public void removeServiceById(long id) {
        for (User user : users.values())
            user.removeServiceById(id);
        services.remove(id);
    }

    @Override
    public int getServiceCount() {
        return services.size();
    }

    @Override
    public long getServiceMaxId() {
        Set keys = services.keySet();
        if (keys.size() == 0)
            return 0;
        else {
            Long[] ids = new Long[keys.size()];
            keys.toArray(ids);
            Arrays.sort(ids);
            return ids[ids.length - 1];
        }
    }

    public String getServiceData() {
        return Models.servicesToString(services);
    }

    @Override
    public Service getUserServiceByType(long userID, String serviceType) throws ServiceNotFoundException, UserNotFoundException {
        User user = getUserById(userID);
        long serviceId;
        Service service;
        try {
            serviceId = user.getServiceIdByType(serviceType);
            service = services.get(serviceId);
            if (service == null)
                throw new ServiceNotFoundException("Service with of type " + serviceType + " at user with id " + userID + " does not exist anymore");
        } catch (ServiceNotFoundException e) {
            throw new ServiceNotFoundException("Service of type " + serviceType + " at user with id " + userID + " not found");
        }
        return service;
    }

    @Override
    public void setServiceToUser(long userId, long serviceId) throws ServiceNotFoundException, UserNotFoundException {
        User user = getUserById(userId);
        Service service = getServiceById(serviceId);
        user.addService(service.getType(), serviceId);
    }
}
