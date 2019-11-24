package model;

import model.services.*;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable{
    private static int counter = 0;

    enum Status{ON, OFF}

    class ServiceWrapper<T extends Service> {
        final T service;
        final Date activationDate = new Date();
        Status status = Status.ON;

        public ServiceWrapper(T service) {
            this.service = service;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public Date getActivationDate() {
            return activationDate;
        }
    }

    private String name;
    private String phoneNumber;
    private String emailAddress;

    private ServiceWrapper<Internet> internet;
    private ServiceWrapper<Phone> phone;
    private ServiceWrapper<Television> television;

    private final int objectID = counter++;

    public User(){}
    private User(User user){
        this.name = user.name;
        this.phoneNumber = user.phoneNumber;
        this.emailAddress = user.emailAddress;
        this.internet = user.internet == null ? null : new ServiceWrapper<>(user.internet.service);
        this.television = user.television == null ? null : new ServiceWrapper<>(user.television.service);
        this.phone = user.phone == null ? null : new ServiceWrapper<>(user.phone.service);
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

    public ServiceWrapper<Internet> getInternet() {
        return internet;
    }

    public void setInternet(Internet internet) {
        this.internet = new ServiceWrapper<>(internet);
    }

    public ServiceWrapper<Phone> getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = new ServiceWrapper<>(phone);
    }

    public ServiceWrapper<Television> getTelevision() {
        return television;
    }

    public void setTelevision(Television television) {
        this.television = new ServiceWrapper<>(television);
    }

    public int getObjectID() {
        return objectID;
    }

}
