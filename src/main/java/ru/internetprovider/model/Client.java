package ru.internetprovider.model;

import ru.internetprovider.model.services.*;

import java.util.List;

public class Client {

    private final long id;
    private String name;
    private String phone;
    private String email;
    private List<ClientInternet> clientInternetList;
    private List<ClientPhone> clientPhoneList;
    private List<ClientTelevision> clientTelevisionList;

    public Client(long id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public Client(String name, String phone, String email) {
        this.id = 0;
        this.name = name;
        this.phone = phone;
        this.email = email;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ClientInternet> getClientInternetList() {
        return clientInternetList;
    }

    public void setClientInternetList(List<ClientInternet> clientInternetList) {
        this.clientInternetList = clientInternetList;
    }

    public List<ClientPhone> getClientPhoneList() {
        return clientPhoneList;
    }

    public void setClientPhoneList(List<ClientPhone> clientPhoneList) {
        this.clientPhoneList = clientPhoneList;
    }

    public List<ClientTelevision> getClientTelevisionList() {
        return clientTelevisionList;
    }

    public void setClientTelevisionList(List<ClientTelevision> clientTelevisionList) {
        this.clientTelevisionList = clientTelevisionList;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phone + '\'' +
                ", emailAddress='" + email + '\'' +
                '}';
    }
}