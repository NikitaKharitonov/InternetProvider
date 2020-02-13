package controller;

import model.services.Internet;
import model.services.Service;
import model.users.User;

import java.util.ArrayList;
import java.util.Date;

/**
 * Interface Controller
 * @author anteii
 * @version 0.2
 * */
public interface Controller {
    /**
     * Creates new user with such parameters {@code params}
     * @param user
     *          User object
     * @throws FailedOperation
     *          If some troubles were happened
     * */
    void      createUser(User user) throws FailedOperation;
    /**
     * Creates new tariff based on {@code service}
     * @param service
     *          Configured Service object
     * @throws FailedOperation
     *          If some troubles were happened
     * */
    void    createService(Service service) throws FailedOperation;
    /**
     * Search user by his id
     * @param userID
     *          user id
     * @throws FailedOperation
     *          If some troubles were happened
     * @return Created Service object
     * */
    User      getUser(int userID) throws FailedOperation;
    /**
     * Return all supported services names
     * @return array of supported services names
     * */
    String[]  getProvidedServices();
    /**
     * Search tariff by its id and name of its service
     * @param serviceID
     *          Service id
     * @throws FailedOperation
     *          If some troubles were happened
     * @return Service object from storage
     * */
    Service    getService(long serviceID) throws FailedOperation;
    /**
     * Returns all tariffs of {@code serviceName}
     * @param serviceType
     *          Service type
     * @throws FailedOperation
     *          If some troubles were happened
     * @return Array of existing Services
     * */
    ArrayList<Service>  getAllServices(String serviceType) throws FailedOperation;
    /**
     * Set {@code service} to user by his id
     * @param userID
     *          User id
     * @throws FailedOperation
     *          If some troubles were happened
     * */
    void    setServiceToUser(long userID, Service service, Date date) throws FailedOperation;
    /**
     * Change user data (E.g. name or email address)
     * @param user
     *          User object wich will be set up by userID
     * @throws FailedOperation
     *          If some troubles were happened
     * */
    void      changeUserData(User user) throws FailedOperation;
    /**
     * Change tariff parameters
     * @param service
     *          Service object wich will be changed
     * @throws FailedOperation
     *          If some troubles were happened
     * */
    void    changeService(Service service) throws FailedOperation;
    /**
     * Disable {@code serviceName} for user with such id
     * @param userID
     *          User id
     * @param serviceType
     *          Service type (e.g. Internet or Television)
     * @throws FailedOperation
     *          If some troubles were happened
     * */
    void    removeServiceFromUser(long userID, String serviceType) throws FailedOperation;
    /**
     * Delete user
     * @param userID
     *          User id
     * @throws FailedOperation
     *          If some troubles were happened
     * */
    void      deleteUser(long userID) throws FailedOperation;
    /**
     * Delete tariff and instantly save changes in a storage
     * @param serviceID
     *          Service id
     * */
    void    deleteService(long serviceID) throws FailedOperation;

    long getNextServiceId();
    long getNextUserId();
    Internet.ConnectionType getConnectionType(String connectionType);
    String getUsersData();
    String getServicesData();
}
