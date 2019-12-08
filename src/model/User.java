package model;

import model.services.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class User {

    private class ActivatedService {

        final Service service;
        final String activationDate;

        public ActivatedService(Service service) {
            this.service = service;
            activationDate = new Date().toString().replaceAll(" ", "_");
        }

        public ActivatedService(Service service, String activationDate) {
            this.service = service;
            this.activationDate = activationDate;
        }
    }

    private final long id;
    private String name;
    private String phoneNumber;
    private String emailAddress;

    private HashMap<String, ActivatedService> activatedServiceHashMap;

    public User(long id, String name, String phoneNumber, String emailAddress) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.activatedServiceHashMap = new HashMap<>();
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

    public long getServiceIdByType(String serviceType) throws ServiceNotFoundException {
        ActivatedService activatedService = activatedServiceHashMap.get(serviceType);
        if (activatedService == null)
            throw new ServiceNotFoundException("Service of type " + serviceType + " not found");
        return activatedService.service.getId();
    }

    public String getActivationDateByType(String serviceType) throws ServiceNotFoundException {
        ActivatedService activatedService = activatedServiceHashMap.get(serviceType);
        if (activatedService == null)
            throw new ServiceNotFoundException("Service of type " + serviceType + " not found");
        return activatedService.activationDate;
    }

    public void addService(Service service) {
        activatedServiceHashMap.put(service.getType(), new ActivatedService(service));
    }

    public void addService(Service service, String activationDate) {
        activatedServiceHashMap.put(service.getType(), new ActivatedService(service, activationDate));
    }

    public void removeService(String serviceType) {
        activatedServiceHashMap.remove(serviceType);
    }

    public String[] getServiceTypes() {
        Set<String> stringSet = activatedServiceHashMap.keySet();
        String[] strings = new String[stringSet.size()];
        stringSet.toArray(strings);
        return strings;
    }


}
