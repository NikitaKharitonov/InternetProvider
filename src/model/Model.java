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

    private final String usersDataPath = "/data/users";
    private final String internetsDataPath = "/data/internets";
    private final String phonesDataPath = "/data/phones";
    private final String televisionsDataPath = "/data/televisions";

    public Model(ArrayList<User> users) {
        this.users = users;
    }

    public Model() throws IOException, ClassNotFoundException {
        ObjectInputStream usersData = new ObjectInputStream(new FileInputStream(System.getProperty("user.dir") + usersDataPath));
        ObjectInputStream internetsData = new ObjectInputStream(new FileInputStream(System.getProperty("user.dir") + internetsDataPath));
        ObjectInputStream phonesData = new ObjectInputStream(new FileInputStream(System.getProperty("user.dir") + phonesDataPath));
        ObjectInputStream televisionsData = new ObjectInputStream(new FileInputStream(System.getProperty("user.dir") + televisionsDataPath));
        users = (ArrayList<User>) usersData.readObject();
        internets = (ArrayList<Internet>) internetsData.readObject();
        phones = (ArrayList<Phone>) phonesData.readObject();
        televisions = (ArrayList<Television>) televisionsData.readObject();
    }

    public void save() throws IOException {
        ObjectOutputStream usersData = new ObjectOutputStream(new FileOutputStream(System.getProperty("user.dir") +usersDataPath));
        ObjectOutputStream internetsData = new ObjectOutputStream(new FileOutputStream(System.getProperty("user.dir") +internetsDataPath));
        ObjectOutputStream phonesData = new ObjectOutputStream(new FileOutputStream(System.getProperty("user.dir") + phonesDataPath));
        ObjectOutputStream televisionsData = new ObjectOutputStream(new FileOutputStream(System.getProperty("user.dir") + televisionsDataPath));
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

    public void deleteUserById(int id) {
        users.remove(id);
    }

    public Internet getInternetById(int id) {
        return internets.get(id);
    }

    public void addInternet(Internet internet) {
        internets.add(internet);
    }

    public void deleteInternetById(int id) {internets.remove(id);}

    public Phone getPhoneById(int id) {
        return phones.get(id);
    }

    public void addPhone(Phone phone) {
        phones.add(phone);
    }

    public void deletePhoneById(int id) {phones.remove(id);}

    public Television getTelevisionById(int id) {
        return televisions.get(id);
    }

    public void addTelevision(Television television) {
        televisions.add(television);
    }

    public void deleteTelevisionById(int id) {televisions.remove(id);}

    public int getUserCount() {
        return users.size();
    }

    public int getInternetCount() {
        return internets.size();
    }

    public int getPhoneCount() {
        return phones.size();
    }

    public int getTelevisionCount() {
        return televisions.size();
    }

    public void setUserById(int id, User user){
        users.set(id, user);
    }
    public void setInternetById(int id, Internet internet){
        internets.set(id, internet);
    }
    public void setTelevisionById(int id, Television television){
        televisions.set(id, television);
    }
    public void setPhoneById(int id, Phone phone){
        phones.set(id, phone);
    }

    public ArrayList<Internet> getInternets() {
        return internets;
    }

    public ArrayList<Phone> getPhones() {
        return phones;
    }

    public ArrayList<Television> getTelevisions() {
        return televisions;
    }
}

