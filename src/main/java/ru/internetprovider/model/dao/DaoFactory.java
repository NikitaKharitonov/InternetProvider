package ru.internetprovider.model.dao;

public interface DaoFactory {
    ClientDao createClientDao();
    InternetDao createInternetDao();
    PhoneDao createPhoneDao();
    TelevisionDao createTelevisionDao();
}
