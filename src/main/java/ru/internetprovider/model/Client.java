package ru.internetprovider.model;

import ru.internetprovider.model.services.ClientService;
import ru.internetprovider.model.services.Internet;
import ru.internetprovider.model.services.Phone;
import ru.internetprovider.model.services.Television;

import java.util.List;

public class Client {

    private final long id;
    private String name;
    private String phone;
    private String email;
    private List<ClientService<Internet>> internetClientServiceList;
    private List<ClientService<Phone>> phoneClientServiceList;
    private List<ClientService<Television>> televisionClientServiceList;

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

    public List<ClientService<Internet>> getInternetClientServiceList() {
        return internetClientServiceList;
    }

    public void setInternetClientServiceList(List<ClientService<Internet>> internetClientServiceList) {
        this.internetClientServiceList = internetClientServiceList;
    }

    public List<ClientService<Phone>> getPhoneClientServiceList() {
        return phoneClientServiceList;
    }

    public void setPhoneClientServiceList(List<ClientService<Phone>> phoneClientServiceList) {
        this.phoneClientServiceList = phoneClientServiceList;
    }

    public List<ClientService<Television>> getTelevisionClientServiceList() {
        return televisionClientServiceList;
    }

    public void setTelevisionClientServiceList(List<ClientService<Television>> televisionClientServiceList) {
        this.televisionClientServiceList = televisionClientServiceList;
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