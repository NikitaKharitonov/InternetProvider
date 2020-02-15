package model.models;

import model.data_storage_factories.DataStorageFactory;
import model.data_storage_factories.XMLFileDataStorageFactory;
import model.exceptions.ServiceNotFoundException;
import model.exceptions.UserNotFoundException;
import model.services.Service;
import model.users.User;

import java.io.IOException;
import java.util.*;

public class BaseModel implements Model {
    private HashMap<Long, User> users;
    private HashMap<Long, Service> services;
    private DataStorageFactory dataStorageFactory = new XMLFileDataStorageFactory();

    public BaseModel() throws IOException {
        read();
    }

    public void setDataStorageFactory(DataStorageFactory factory) {
        dataStorageFactory = factory;
    }

    public void read() throws IOException {
        if ((users = dataStorageFactory.readUsers()) == null)
            users = new HashMap<>();
        if ((services = dataStorageFactory.readServices()) == null)
            services = new HashMap<>();
    }

    @Override
    public void save() throws IOException {
        dataStorageFactory.writeUsers(users);
        dataStorageFactory.writeServices(services);
    }

    @Override
    public User getUserById(long id) throws UserNotFoundException {
        User user = users.get(id);
        if (user == null)
            throw new UserNotFoundException("User with id " + id + " not found");
        return user;
    }

    @Override
    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void removeUserById(long id) throws UserNotFoundException {
        users.remove(id);
    }

    @Override
    public int getUserCount() {
        return users.size();
    }

    @Override
    public long getUserMaxId() {
        if (users.size() == 0)
            return 0;
        Set<Long> set = users.keySet();
        Long[] longs = new Long[set.size()];
        set.toArray(longs);
        Arrays.sort(longs);
        return longs[longs.length - 1];
    }

    @Override
    public String getUserData() {
        return users.toString();
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
    public void removeServiceById(long id) throws ServiceNotFoundException {
        //fixme
//        for (User user : userMap.users())
//            user.removeServiceById(id);
//        serviceMap.remove(id);
    }

    @Override
    public int getServiceCount() {
        return services.size();
    }

    @Override
    public long getServiceMaxId() {
        if (services.size() == 0)
            return 0;
        Set<Long> set = services.keySet();
        Long[] longs = new Long[set.size()];
        set.toArray(longs);
        Arrays.sort(longs);
        return longs[longs.length - 1];
    }

    public String getServiceData() {
        return services.toString();
    }

    @Override
    public Service getUserServiceByType(long userID, String serviceType) throws ServiceNotFoundException, UserNotFoundException {
        return null;
    }

    @Override
    public void addServiceToUserById(long userID, Service service, Date date, User.Status status) throws UserNotFoundException {
        this.addService(service);
        this.getUserById(userID).addUserService(service, date, status);
    }

    // fixme
    public User getUserByUsername(String username) throws UserNotFoundException {
        for (User user : users.values())
            if (user.getUsername().equals(username))
                return user;
        throw new UserNotFoundException("Username " + username + " not found");
    }

}
