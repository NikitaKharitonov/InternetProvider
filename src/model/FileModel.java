package model;

import model.services.*;

import java.io.*;
import java.util.*;

public class FileModel implements Model {

    private HashMap<Long, User> users;
    private HashMap<Long, Service> services;

    private final String projectDir = System.getProperty("user.dir");
    private final String serviceDataPath = projectDir + "/data/services";
    private final String usersDataPath = projectDir + "/data/users";

    private void writeUsers() throws IOException {
        FileWriter writer = new FileWriter(usersDataPath);
        writer.write(users.size() + " ");
        for (Long id : users.keySet()) {
            User user = users.get(id);
            writer.write(id + " ");
            writer.write(user.getName() + " ");
            writer.write(user.getPhoneNumber() + " ");
            writer.write(user.getEmailAddress() + " ");
            ActivatedService[] activatedServices = user.getServiceMap().getServices();
            writer.write(activatedServices.length + " ");
            for (ActivatedService activatedService : activatedServices) {
                writer.write(activatedService.getServiceID() + " ");
                writer.write(activatedService.getType() + " ");
                writer.write(activatedService.getActivationDate() + " ");
            }
        }
        writer.close();
    }

    private HashMap<Long, User> readUsers() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(usersDataPath));
        String line = reader.readLine();
        if (line == null)
            return null;
        String[] words = line.split(" ");
        int index = 0;
        int size = Integer.parseInt(words[index++]);
        HashMap<Long, User> users = new HashMap<>();
        Long id;
        String name, emailAddress, phoneNumber;
        for (int i = 0; i < size; ++i) {
            id = Long.valueOf(words[index++]);
            name = words[index++];
            emailAddress = words[index++];
            phoneNumber = words[index++];
            int activatedServicesCount = Integer.valueOf(words[index++]);
            ActivatedService[] activatedServices = new ActivatedService[activatedServicesCount];
            for (int j = 0; j < activatedServicesCount; ++j) {
                int serviceID = Integer.valueOf(words[index++]);
                String type = words[index++];
                String activationDate = words[index++];
                activatedServices[j] = new ActivatedService(serviceID, type, activationDate);
            }
            User user = new User(id, name, emailAddress, phoneNumber, new ServiceMap(activatedServices));
            users.put(id, user);
        }
        reader.close();
        return users;
    }

    private void writeServices() throws IOException {
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
                    writer.write(television.getNumberOfChannels());
                    break;
            }
        }
        writer.close();
    }

    private HashMap<Long, Service> readServices() throws IOException {
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
                    int speed = Integer.valueOf(words[index++]);
                    boolean antivirus = Boolean.valueOf(words[index++]);
                    Internet.ConnectionType connectionType = Internet.ConnectionType.valueOf(words[index++]);
                    service = new Internet(id, name, speed, antivirus, connectionType);
                    break;
                case "Phone":
                    int callsMinCount = Integer.valueOf(words[index++]);
                    int smsCount = Integer.valueOf(words[index++]);
                    service = new Phone(id, name, callsMinCount, smsCount);
                    break;
                case "Television":
                    int numberOfChannels = Integer.valueOf(words[index++]);
                    service = new Television(id, name, numberOfChannels);
                    break;
            }
            services.put(id, service);
        }
        reader.close();
        return services;
    }

    public FileModel() throws IOException {
        if ((users = readUsers()) == null)
            users = new HashMap<>();
        if ((services = readServices()) == null)
            services = new HashMap<>();
    }

    public void save() throws IOException {
        writeServices();
        writeUsers();
    }

    @Override
    public User getUserById(long id) {
        return users.get(id);
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
    public Service getServiceById(long id) {
        return services.get(id);
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
}

