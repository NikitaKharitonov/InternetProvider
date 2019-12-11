package server.model.data_storage_factories;

import server.model.services.ServiceMap;
import server.model.users.UserMap;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class TextFileDataStorageFactory implements DataStorageFactory {
    private String usersDataPath = "./data/text/users";
    private String serviceDataPath = "./data/text/services";

    @Override
    public void writeUsers(UserMap users) throws IOException {
        try {
            FileWriter writer = new FileWriter(usersDataPath);
            writer.write(users.toString());
            writer.close();
        } catch (IOException e) {
            throw new IOException("Failed to write users to file");
        }
    }

    @Override
    public UserMap readUsers() throws IOException {
        try {
            Stream<String> lines = Files.lines(Paths.get(usersDataPath), StandardCharsets.UTF_8);
            if (lines == null)
                return null;
            StringBuilder builder = new StringBuilder();
            lines.forEach(s -> builder.append(s).append("\n"));
            return new UserMap(builder.toString());
        } catch (IOException e) {
            throw new IOException("Failed to read users from file");
        }
    }

    @Override
    public void writeServices(ServiceMap serviceMap) throws IOException {
        try {
            FileWriter writer = new FileWriter(serviceDataPath);
            writer.write(serviceMap.toString());
            writer.close();
        } catch (IOException e) {
            throw new IOException("Failed to write services to file");
        }
    }

    @Override
    public ServiceMap readServices() throws IOException {
        try {
            Stream<String> lines = Files.lines(Paths.get(serviceDataPath), StandardCharsets.UTF_8);
            if (lines == null)
                return null;
            StringBuilder builder = new StringBuilder();
            lines.forEach(s -> builder.append(s).append("\n"));
            return new ServiceMap(builder.toString());
        } catch (IOException e) {
            throw new IOException("Failed to read services from file");
        }
    }
}
