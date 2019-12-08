package model;

import model.services.*;

import java.io.*;
import java.util.*;

public class FileModel implements Model {

    private HashMap<Long, User> users; // each key represents the user's ID

    private HashMap<Long, Service> services; // each key represents the service's ID

    private final String projectDir = System.getProperty("user.dir");
    private final String usersDataPath = projectDir + "/data/users"; // file for the services data storage
    private final String serviceDataPath = projectDir + "/data/services"; // file for the services data storage

    /*
    Writes the users data to the file
     */
    private void writeUsers() throws IOException {
        try {
            FileWriter writer = new FileWriter(usersDataPath);
            writer.write(users.size() + " ");
            for (Long id : users.keySet()) {
                User user = users.get(id);
                writer.write(id + " ");
                writer.write(user.getName() + " ");
                writer.write(user.getPhoneNumber() + " ");
                writer.write(user.getEmailAddress() + " ");

                // Check for deleted services
                String[] serviceTypes = user.getServiceTypes();
                for (String serviceType: serviceTypes) {
                    long serviceId = user.getServiceIdByType(serviceType);
                    if (services.get(serviceId) == null)
                        user.removeService(serviceType);
                }

                serviceTypes = user.getServiceTypes();
                writer.write(serviceTypes.length + " ");
                for (String serviceType: serviceTypes) {
                    writer.write(user.getServiceIdByType(serviceType) + " ");
                    writer.write(serviceType + " ");
                    writer.write(user.getActivationDateByType(serviceType) + " ");
                }
            }
            writer.close();
        } catch (IOException | ServiceNotFoundException e) {
            throw new IOException("Failed to write users to file");
        }
    }

    /*
    Reads and returns the users data from the file
     */
    private HashMap<Long, User> readUsers() throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(usersDataPath));
            String line = reader.readLine();
            if (line == null)
                return null;
            String[] words = line.split(" ");
            int index = 0;
            int size = Integer.parseInt(words[index++]);
            HashMap<Long, User> users = new HashMap<>();
            long id;
            String name, emailAddress, phoneNumber;
            for (int i = 0; i < size; ++i) {
                id = Long.parseLong(words[index++]);
                name = words[index++];
                emailAddress = words[index++];
                phoneNumber = words[index++];
                User user = new User(id, name, emailAddress, phoneNumber);
                int activatedServicesCount = Integer.parseInt(words[index++]);
                for (int j = 0; j < activatedServicesCount; ++j) {
                    long serviceID = Integer.parseInt(words[index++]);
                    String serviceType = words[index++];
                    String activationDate = words[index++];
                    user.addService(services.get(serviceID), activationDate);
                }
                users.put(id, user);
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
    private void writeServices() throws IOException {
        try {
            FileWriter writer = new FileWriter(serviceDataPath);
            writer.write(services.size() + " ");
            for (Long id : services.keySet()) {
                Service service = services.get(id);
                writer.write(id + " ");
                writer.write(service.getType() + " ");
                writer.write(service.getName() + " ");
                switch (service.getType()) {
                    case "Internet":
                        Internet internet = (Internet) service;
                        writer.write(internet.getSpeed() + " ");
                        writer.write(internet.isAntivirus() + " ");
                        writer.write(internet.getConnectionType() + " ");
                        break;
                    case "Phone":
                        Phone phone = (Phone) service;
                        writer.write(phone.getCallsMinCount() + " ");
                        writer.write(phone.getSmsCount() + " ");
                        break;
                    case "Television":
                        Television television = (Television) service;
                        writer.write(television.getNumberOfChannels() + " ");
                        break;
                }
            }
            writer.close();
        } catch (IOException e) {
            throw new IOException("Failed to write services to file");
        }
    }

    /*
    Reads and returns the services data from the file
     */
    private HashMap<Long, Service> readServices() throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(serviceDataPath));
            String line = reader.readLine();
            if (line == null)
                return null;
            String[] words = line.split(" ");
            int index = 0;
            int size = Integer.parseInt(words[index++]);
            HashMap<Long, Service> services = new HashMap<>();
            Long id;
            String type, name;
            for (int i = 0; i < size; ++i) {
                id = Long.valueOf(words[index++]);
                type = words[index++];
                name = words[index++];
                Service service = null;
                switch (type) {
                    case "Internet":
                        int speed = Integer.parseInt(words[index++]);
                        boolean antivirus = Boolean.parseBoolean(words[index++]);
                        Internet.ConnectionType connectionType = Internet.ConnectionType.valueOf(words[index++]);
                        service = new Internet(id, name, speed, antivirus, connectionType);
                        break;
                    case "Phone":
                        int callsMinCount = Integer.parseInt(words[index++]);
                        int smsCount = Integer.parseInt(words[index++]);
                        service = new Phone(id, name, callsMinCount, smsCount);
                        break;
                    case "Television":
                        int numberOfChannels = Integer.parseInt(words[index++]);
                        service = new Television(id, name, numberOfChannels);
                        break;
                }
                services.put(id, service);
            }
            reader.close();
            return services;
        } catch (IOException e) {
            throw new IOException("Failed to read services from file");
        }
    }

    public FileModel() throws IOException {
        if ((services = readServices()) == null)
            services = new HashMap<>();
        if ((users = readUsers()) == null)
            users = new HashMap<>();

    }

    public void save() throws IOException {
        writeServices();
        writeUsers();
    }

    @Override
    public User getUserById(long id) throws UserNotFoundException {
        User user = users.get(id);
        if(user == null)
            throw new UserNotFoundException("User with id " + id + " not found");
        return user;
    }

    @Override
    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void removeUserById(long id) {
        users.remove(id);
    }

    @Override
    public int getUserCount() {
        return users.size();
    }

    @Override
    public long getUserMaxId() {
        Set keys = users.keySet();
        if (keys.size() == 0)
            return 0;
        else {
            Long[] ids = new Long[keys.size()];
            keys.toArray(ids);
            Arrays.sort(ids);
            return ids[ids.length - 1];
        }
    }

    @Override
    public Service getServiceById(long id) throws ServiceNotFoundException {
        Service service = services.get(id);
        if (service == null)
            throw new ServiceNotFoundException("Service with id " + id + " not found");
        return service;
    }

    @Override
    public void addService(Service service) {
        services.put(service.getId(), service);
    }

    @Override
    public void removeServiceById(long id) {
        services.remove(id);
    }

    @Override
    public int getServiceCount() {
        return services.size();
    }

    @Override
    public long getServiceMaxId() {
        Set keys = services.keySet();
        if (keys.size() == 0)
            return 0;
        else {
            Long[] ids = new Long[keys.size()];
            keys.toArray(ids);
            Arrays.sort(ids);
            return ids[ids.length - 1];
        }
    }

    @Override
    public Service getUserServiceByType(long userID, String serviceType) throws ServiceNotFoundException, UserNotFoundException {
        User user = getUserById(userID);
        long serviceId;
        Service service;
        try {
            serviceId = user.getServiceIdByType(serviceType);
            service = services.get(serviceId);
            if (service == null)
                throw new ServiceNotFoundException("Service with of type " + serviceType + " at user with id " + userID + " does not exist anymore");
        } catch (ServiceNotFoundException e) {
            throw new ServiceNotFoundException("Service of type " + serviceType + " at user with id " + userID + " not found");
        } finally {
            try {
                save();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        return service;
    }

    @Override
    public void setServiceToUser(long userId, long serviceId) throws ServiceNotFoundException, UserNotFoundException {
        User user = getUserById(userId);
        Service service = getServiceById(serviceId);
        user.addService(service);
    }
}

