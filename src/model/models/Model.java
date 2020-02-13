package model.models;

import model.data_storage_factories.DataStorageFactory;
import model.exceptions.ServiceNotFoundException;
import model.exceptions.UserNotFoundException;
import model.services.Service;
import model.users.User;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;

public interface Model {

    void setDataStorageFactory(DataStorageFactory dataStorageFactory);

    void read() throws IOException;

    void save() throws IOException;

    User getUserById(long id) throws UserNotFoundException;

    void addUser(User user);

    void removeUserById(long id) throws UserNotFoundException;

    int getUserCount();

    long getUserMaxId();

    String getUserData();

    Service getServiceById(long id) throws ServiceNotFoundException;

    void addService(Service service);

    void removeServiceById(long id) throws ServiceNotFoundException;

    int getServiceCount();

    long getServiceMaxId();

    String getServiceData();

    Service getUserServiceByType(long userID, String serviceType) throws ServiceNotFoundException, UserNotFoundException;

    void addServiceToUserById(long userID, Service service, Date date, User.Status status) throws UserNotFoundException;

    User getUserByName(String name) throws UserNotFoundException;
}
