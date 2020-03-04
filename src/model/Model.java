package model;

import model.exceptions.InvalidModelException;
import model.exceptions.ServiceNotFoundException;
import model.exceptions.ClientNotFoundException;
import model.services.Service;

import java.util.List;

public interface Model {

    Client getClient(long id) throws ClientNotFoundException, InvalidModelException;

    List<Client> getClients() throws InvalidModelException;

    void addClient(Client client) throws InvalidModelException;

    void removeClient(long id) throws ClientNotFoundException, InvalidModelException;

    int getClientsCount() throws InvalidModelException;

    List<Service> getClientServicesByType(long clientID, String serviceType)
            throws ServiceNotFoundException, ClientNotFoundException, InvalidModelException;

    void addServiceToClient(long clientID, Service service) throws ClientNotFoundException, InvalidModelException;

}
