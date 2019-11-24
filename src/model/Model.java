package model;

import model.services.Internet;
import model.services.Phone;
import model.services.Television;

import java.io.IOException;
import java.util.ArrayList;

public interface Model {

    public void save() throws IOException;

    User getUserById(int id);

    void addUser(User user);

    void deleteUserById(int id);

    Internet getInternetById(int id);

    void addInternet(Internet internet);

    void deleteInternetById(int id);

    Phone getPhoneById(int id);

    void addPhone(Phone phone);

    void deletePhoneById(int id);

    Television getTelevisionById(int id);

    void addTelevision(Television television);

    void deleteTelevisionById(int id);

    int getUserCount();

    int getInternetCount();

    int getPhoneCount();

    int getTelevisionCount();

    void setUserById(int id, User user);

    void setInternetById(int id, Internet internet);

    void setTelevisionById(int id, Television television);

    void setPhoneById(int id, Phone phone);

    ArrayList<Internet> getInternets();

    ArrayList<Phone> getPhones();

    ArrayList<Television> getTelevisions();
}
