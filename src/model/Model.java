package model;

import model.services.Service;

import java.io.IOException;

public interface Model {

    void save() throws IOException;

    User getUserById(long id) throws UserNotFoundException;

    void addUser(User user);

    void removeUserById(long id);

    int getUserCount();

    long getUserMaxId();

    Service getServiceById(long id) throws ServiceNotFoundException;

    void addService(Service service);

    void removeServiceById(long id);

    int getServiceCount();

    long getServiceMaxId();

    Service getUserServiceByType(long userID, String serviceType) throws ServiceNotFoundException, UserNotFoundException;

    void setServiceToUser(long userId, long serviceId) throws ServiceNotFoundException, UserNotFoundException;
}
