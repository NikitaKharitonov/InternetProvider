package ru.internetprovider.model.dao;

/**
 * The implementation of the Abstract Factory pattern.
 * Provides methods for creation of abstract DAO objects.
 */
public interface DaoFactory {
    ClientDao createClientDao();
    InternetDao createInternetDao();
    PhoneDao createPhoneDao();
    TelevisionDao createTelevisionDao();
}
