package model;

import model.services.*;

import java.io.Serializable;

public class User implements Serializable, Cloneable{
    private String name;
    private String phoneNumber;
    private String emailAddress;
    private Internet internet;
    private Phone phone;
    private Television television;

    public User(){}
    private User(User user){
        this.name = user.name;
        this.phoneNumber = user.phoneNumber;
        this.emailAddress = user.emailAddress;
        this.internet = (Internet) user.internet.clone();
        this.television = (Television) user.television.clone();
        this.phone = (Phone) user.phone.clone();
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

    public Internet getInternet() {
        return internet;
    }

    public void setInternet(Internet internet) {
        this.internet = internet;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Television getTelevision() {
        return television;
    }

    public void setTelevision(Television television) {
        this.television = television;
    }

    @Override
    public Object clone(){
        //deep copy
        return new User(this);
    }
}
