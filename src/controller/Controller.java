package controller;

import model.services.Internet;
import model.services.Service;
import model.Client;

import java.util.ArrayList;
import java.util.Date;

/**
 * Interface Controller
 * @author anteii
 * @version 0.2
 * */
public interface Controller {
    /**
     * Creates new client with such parameters {@code params}
     * @param client
     *          Client object
     * @throws FailedOperation
     *          If some troubles were happened
     * */
    void      createClient(Client client) throws FailedOperation;
    /**
     * Creates new tariff based on {@code service}
     * @param service
     *          Configured Service object
     * @throws FailedOperation
     *          If some troubles were happened
     * */
    void    createService(Service service) throws FailedOperation;
    /**
     * Search client by his id
     * @param userID
     *          client id
     * @throws FailedOperation
     *          If some troubles were happened
     * @return Created Service object
     * */
    Client getClientById(int userID) throws FailedOperation;
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
     * Set {@code service} to client by his id
     * @param userID
     *          Client id
     * @throws FailedOperation
     *          If some troubles were happened
     * */
    void    setServiceToClient(long userID, Service service, Date date) throws FailedOperation;
    /**
     * Change client data (E.g. name or email address)
     * @param client
     *          Client object wich will be set up by userID
     * @throws FailedOperation
     *          If some troubles were happened
     * */
    void      changeClientData(Client client) throws FailedOperation;
    /**
     * Change tariff parameters
     * @param service
     *          Service object wich will be changed
     * @throws FailedOperation
     *          If some troubles were happened
     * */
    void updateService(Service service) throws FailedOperation;
    /**
     * Disable {@code serviceName} for client with such id
     * @param userID
     *          Client id
     * @param serviceType
     *          Service type (e.g. Internet or Television)
     * @throws FailedOperation
     *          If some troubles were happened
     * */
    void    removeServiceFromClient(long userID, String serviceType) throws FailedOperation;
    /**
     * Delete client
     * @param userID
     *          Client id
     * @throws FailedOperation
     *          If some troubles were happened
     * */
    void      deleteClient(long userID) throws FailedOperation;
    /**
     * Delete tariff and instantly save changes in a storage
     * @param serviceID
     *          Service id
     * */
    void    deleteService(long serviceID) throws FailedOperation;

    long getNextServiceId();
    long getNextClientId();
    Internet.ConnectionType getConnectionType(String connectionType);
    String getClientsData();
    String getServicesData();
}
