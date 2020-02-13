package model.data_storage_factories;

import model.services.Service;
import model.users.User;

import java.io.IOException;
import java.util.HashMap;

public interface DataStorageFactory {
    void writeUsers(HashMap<Long, User> users) throws IOException;
    HashMap<Long, User> readUsers() throws IOException;
    void writeServices(HashMap<Long, Service> services) throws IOException;
    HashMap<Long, Service> readServices() throws IOException;
}
