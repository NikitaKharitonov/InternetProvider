package model;

import model.services.Internet;
import model.services.Phone;
import model.services.Television;

import java.io.*;
import java.util.ArrayList;

public class Model {

    private ArrayList<User> users;
    private ArrayList<Internet> internets;
    private ArrayList<Phone> phones;
    private ArrayList<Television> televisions;

    private final String usersDataPath = "model/storage/users";
    private final String internetsDataPath = "model/storage/internets";
    private final String phonesDataPath = "model/storage/phones";
    private final String televisionsDataPath = "model/storage/televisions";

    public Model(ArrayList<User> users) {
        this.users = users;
    }

    public Model() throws IOException, ClassNotFoundException {
        ObjectInputStream usersData = new ObjectInputStream(new FileInputStream(usersDataPath));
        ObjectInputStream internetsData = new ObjectInputStream(new FileInputStream(internetsDataPath));
        ObjectInputStream phonesData = new ObjectInputStream(new FileInputStream(phonesDataPath));
        ObjectInputStream televisionsData = new ObjectInputStream(new FileInputStream(televisionsDataPath));
        users = (ArrayList<User>) usersData.readObject();
        internets = (ArrayList<Internet>) internetsData.readObject();
        phones = (ArrayList<Phone>) phonesData.readObject();
        televisions = (ArrayList<Television>) televisionsData.readObject();
    }

    public void save() throws IOException {
        ObjectOutputStream usersData = new ObjectOutputStream(new FileOutputStream(usersDataPath));
        ObjectOutputStream internetsData = new ObjectOutputStream(new FileOutputStream(internetsDataPath));
        ObjectOutputStream phonesData = new ObjectOutputStream(new FileOutputStream(phonesDataPath));
        ObjectOutputStream televisionsData = new ObjectOutputStream(new FileOutputStream(televisionsDataPath));
        usersData.writeObject(users);
        internetsData.writeObject(internets);
        phonesData.writeObject(phones);
        televisionsData.writeObject(televisions);
    }

    public User getUserById(int id) {
        return users.get(id);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public Internet getInternetById(int id) {
        return internets.get(id);
    }

    public void addInternet(Internet internet) {
        internets.add(internet);
    }

    public Phone getPhoneById(int id) {
        return phones.get(id);
    }

    public void addPhone(Phone phone) {
        phones.add(phone);
    }

    public Television getTelevisionById(int id) {
        return televisions.get(id);
    }

    public void addTelevision(Television television) {
        televisions.add(television);
    }
}

// test1
