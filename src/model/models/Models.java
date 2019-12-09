package model.models;

import model.services.*;
import model.users.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Models {
    /*
    Writes the users data to the file
    */
    public static void writeUsersToFile(HashMap<Long, User> users, String filePath) throws IOException {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(usersToString(users));
        } catch (IOException e) {
            throw new IOException("Failed to write users to file");
        }
    }

    /*
    Writes the users data to the string
    */
    public static String usersToString(HashMap<Long, User> users) {
        StringBuilder builder = new StringBuilder();
        for (Long id : users.keySet())
            builder.append(users.get(id).toString() + "\n");
        return builder.toString();
    }

    /*
    Reads and returns the users data from the file
     */
    public static HashMap<Long, User> readUsersFromFile(String filePath) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            HashMap<Long, User> users = new HashMap<>();
            while ((line = reader.readLine()) != null) {
                User user = new User(line);
                users.put(user.getId(), user);
            }
            reader.close();
            return users;
        } catch (IOException e) {
            throw new IOException("Failed to read users from file");
        }
    }

    /*
    Writes the services data to the file
     */
    public static void writeServicesToFile(HashMap<Long, Service> services, String filePath) throws IOException {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(servicesToString(services));
        } catch (IOException e) {
            throw new IOException("Failed to write services to file");
        }
    }

    /*
    Writes the services data to string
     */
    public static String servicesToString(HashMap<Long, Service> services) {
        StringBuilder builder = new StringBuilder();
        for (Long id : services.keySet())
            builder.append(services.get(id).toString() + "\n");
        return builder.toString();
    }

    /*
    Reads and returns the services data from the file
     */
    public static HashMap<Long, Service> readServicesFromFile(String filePath) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            HashMap<Long, Service> services = new HashMap<>();
            while ((line = reader.readLine()) != null) {
                Service service = ServiceFactory.createService(line);
                services.put(service.getId(), service);
            }
            reader.close();
            return services;
        } catch (IOException e) {
            throw new IOException("Failed to read services from file");
        }
    }
}
