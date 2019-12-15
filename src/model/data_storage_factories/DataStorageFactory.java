package model.data_storage_factories;

import model.services.ServiceMap;
import model.users.UserMap;

import java.io.IOException;

public interface DataStorageFactory {
    void writeUsers(UserMap userMap) throws IOException;
    UserMap readUsers() throws IOException;
    void writeServices(ServiceMap serviceMap) throws IOException;
    ServiceMap readServices() throws IOException;
}
