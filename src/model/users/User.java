package model.users;

import model.ValueReader;
import model.exceptions.ServiceNotFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class User {

    private class ActivatedService {
        final long serviceId;
        final String activationDate;

        public ActivatedService(long serviceId) {
            this.serviceId = serviceId;
            activationDate = new Date().toString().replaceAll(" ", "_");
        }

        public ActivatedService(long serviceId, String activationDate) {
            this.serviceId = serviceId;
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

    public User(String str) {
        ValueReader.setString(str);
        id = Long.parseLong(ValueReader.nextValue());
        name = ValueReader.nextValue();
        emailAddress = ValueReader.nextValue();
        phoneNumber = ValueReader.nextValue();
        int activatedServiceCount = Integer.parseInt(ValueReader.nextValue());
        activatedServiceHashMap = new HashMap<>();
        ValueReader.nextValue();
        for (int i = 0; i < activatedServiceCount; ++i) {
            String type = ValueReader.nextValue();
            long serviceId = Long.parseLong(ValueReader.nextValue());
            String activationDate = ValueReader.nextValue();
            activatedServiceHashMap.put(type, new ActivatedService(serviceId, activationDate));
        }
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
        return activatedService.serviceId;
    }

    public String getActivationDateByType(String serviceType) throws ServiceNotFoundException {
        ActivatedService activatedService = activatedServiceHashMap.get(serviceType);
        if (activatedService == null)
            throw new ServiceNotFoundException("Service of type " + serviceType + " not found");
        return activatedService.activationDate;
    }

    public void addService(String type, long serviceId) {
        activatedServiceHashMap.put(type, new ActivatedService(serviceId));
    }

    public void addService(String type, long serviceId, String activationDate) {
        activatedServiceHashMap.put(type, new ActivatedService(serviceId, activationDate));
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

    public ArrayList<Long> getServiceIds() {
        ArrayList<Long> ids = new ArrayList<>(activatedServiceHashMap.size());
        for (String serviceType: activatedServiceHashMap.keySet())
            ids.add(activatedServiceHashMap.get(serviceType).serviceId);
        return ids;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("{id=");
        builder.append(id);
        builder.append(", ");

        builder.append("name=");
        builder.append(name);
        builder.append(", ");

        builder.append("email_address=");
        builder.append(emailAddress);
        builder.append(", ");

        builder.append("phone_number=");
        builder.append(phoneNumber);
        builder.append(", ");

        builder.append("activated_services_count=");
        builder.append(activatedServiceHashMap.size());
        builder.append(", ");
        builder.append("activated_services={ ");
        for (String serviceType: activatedServiceHashMap.keySet()) {
            builder.append("{");
            builder.append("service_type=");
            builder.append(serviceType);
            builder.append(", ");

            builder.append("service_id=");
            builder.append(activatedServiceHashMap.get(serviceType).serviceId);
            builder.append(", ");

            builder.append("activation_date=");
            builder.append(activatedServiceHashMap.get(serviceType).activationDate);
            builder.append("} ");
        }
        builder.append("}}");
        return builder.toString();
    }
}
