package ru.internetprovider.model;

import ru.internetprovider.model.services.ClientInternet;
import ru.internetprovider.model.services.ClientPhone;
import ru.internetprovider.model.services.ClientTelevision;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "client")
public class Client {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "email_address")
    private String emailAddress;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "client_id")
    private List<ClientInternet> clientInternetList;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "client_id")
    private List<ClientPhone> clientPhoneList;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "client_id")
    private List<ClientTelevision> clientTelevisionList;

    public Client() {
    }

    public Client(String name, String phoneNumber, String emailAddress) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public Client(int id, String name, String phoneNumber, String emailAddress) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                ", phoneNumber='" + phoneNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", clientInternetList=" + clientInternetList +
                ", clientPhoneList=" + clientPhoneList +
                ", clientTelevisionList=" + clientTelevisionList +
                '}';
    }
}
