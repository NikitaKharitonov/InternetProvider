package model;

import model.services.Service;

import java.io.IOException;

public interface Model {

    void save() throws IOException;

    User getUserById(long id);

    void addUser(User user);

    void removeUserById(long id);

    int getUserCount();

    long getUserMaxId();

    Service getServiceById(long id);

    void addService(Service service);

    void removeServiceById(long id);

    int getServiceCount();

    long getServiceMaxId();
}
