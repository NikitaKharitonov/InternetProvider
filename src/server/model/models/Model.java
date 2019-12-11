package server.model.models;

import server.model.data_storage_factories.DataStorageFactory;
import server.model.exceptions.ServiceNotFoundException;
import server.model.exceptions.UserNotFoundException;
import server.model.services.Service;
import server.model.users.User;

import java.io.IOException;

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

    void setServiceToUser(long userId, long serviceId) throws ServiceNotFoundException, UserNotFoundException;
}
