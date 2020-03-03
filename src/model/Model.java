package model;

import model.exceptions.InvalidModelException;
import model.exceptions.ServiceNotFoundException;
import model.exceptions.ClientNotFoundException;
import model.services.Service;

public interface Model {

    Client getClient(long id) throws ClientNotFoundException, InvalidModelException;

    void addClient(Client client) throws InvalidModelException;

    void removeClient(long id) throws ClientNotFoundException, InvalidModelException;

    int getClientsCount() throws InvalidModelException;

    Service[] getClientServicesByType(long clientID, String serviceType)
            throws ServiceNotFoundException, ClientNotFoundException, InvalidModelException;

    void addServiceToClient(long clientID, Service service) throws ClientNotFoundException, InvalidModelException;

}
