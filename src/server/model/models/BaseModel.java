package server.model.models;

import server.model.data_storage_factories.DataStorageFactory;
import server.model.data_storage_factories.TextFileDataStorageFactory;
import server.model.exceptions.ServiceNotFoundException;
import server.model.exceptions.UserNotFoundException;
import server.model.services.ServiceMap;
import server.model.users.UserMap;
import server.model.services.Service;
import server.model.users.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class BaseModel implements Model {
    private UserMap userMap;
    private ServiceMap serviceMap;
    private DataStorageFactory dataStorageFactory = new TextFileDataStorageFactory();

    public BaseModel() throws IOException {
        //read();
    }

    public void setDataStorageFactory(DataStorageFactory factory) {
        dataStorageFactory = factory;
    }

    public void read() throws IOException {
        if ((userMap = dataStorageFactory.readUsers()) == null)
            userMap = new UserMap();
        if ((serviceMap = dataStorageFactory.readServices()) == null)
            serviceMap = new ServiceMap();
    }

    @Override
    public void save() throws IOException {
        dataStorageFactory.writeUsers(userMap);
        dataStorageFactory.writeServices(serviceMap);
    }

    @Override
    public User getUserById(long id) throws UserNotFoundException {
        User user = userMap.get(id);
        if (user == null)
            throw new UserNotFoundException("User with id " + id + " not found");
        return user;
    }

    @Override
    public void addUser(User user) {
        userMap.put(user);
    }

    @Override
    public void removeUserById(long id) throws UserNotFoundException {
        userMap.remove(id);
    }

    @Override
    public int getUserCount() {
        return userMap.size();
    }

    @Override
    public long getUserMaxId() {
        if (userMap.size() == 0)
            return 0;
        Set<Long> set = userMap.idSet();
        Long[] longs = new Long[set.size()];
        set.toArray(longs);
        Arrays.sort(longs);
        return longs[longs.length - 1];
    }

    @Override
    public String getUserData() {
        return userMap.toString();
    }

    @Override
    public Service getServiceById(long id) throws ServiceNotFoundException {
        Service service = serviceMap.get(id);
        if (service == null)
            throw new ServiceNotFoundException("Service with id " + id + " not found");
        return service;
    }

    @Override
    public void addService(Service service) {
        serviceMap.put(service);
    }

    @Override
    public void removeServiceById(long id) throws ServiceNotFoundException {
        for (User user : userMap.users())
            user.removeServiceById(id);
        userMap.remove(id);
    }

    @Override
    public int getServiceCount() {
        return serviceMap.size();
    }

    @Override
    public long getServiceMaxId() {
        if (serviceMap.size() == 0)
            return 0;
        Set<Long> set = serviceMap.idSet();
        Long[] longs = new Long[set.size()];
        set.toArray(longs);
        Arrays.sort(longs);
        return longs[longs.length - 1];
    }

    public String getServiceData() {
        return serviceMap.toString();
    }

    @Override
    public Service getUserServiceByType(long userID, String serviceType) throws ServiceNotFoundException, UserNotFoundException {
        return null;
    }

    @Override
    public void setServiceToUser(long userId, long serviceId) throws ServiceNotFoundException, UserNotFoundException {
        User user = getUserById(userId);
        Service service = getServiceById(serviceId);
        user.addService(service.getType(), serviceId);
    }
}
