package model;

import model.services.ServiceMap;
import model.services.Service;
import model.services.ActivatedService;

public class User {
    private final long id;
    private String name;
    private String phoneNumber;
    private String emailAddress;

    private ServiceMap serviceMap;

    public User(long id, String name, String phoneNumber, String emailAddress, ServiceMap serviceMap) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.serviceMap = serviceMap;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public ServiceMap getServiceMap() {
        return serviceMap;
    }

    public void putService(Service service) {
        serviceMap.put(new ActivatedService(service.getId(), service.getClass().getSimpleName()));
    }
}
