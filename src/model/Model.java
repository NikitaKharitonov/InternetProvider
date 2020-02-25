package model;

import model.exceptions.ServiceNotFoundException;
import model.exceptions.UserNotFoundException;
import model.services.Service;

public interface Model {

    User getUser(long id) throws UserNotFoundException;

    void addUser(User user);

    void removeUser(long id) throws UserNotFoundException;

    int getUserCount();

    Service[] getUserServicesByType(long userID, String serviceType) throws ServiceNotFoundException, UserNotFoundException;

    void addServiceToUser(long userID, Service service) throws UserNotFoundException;

}
