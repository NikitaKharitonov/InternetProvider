package model;

import model.exceptions.ClientNotFoundException;
import model.exceptions.InvalidModelException;
import model.exceptions.ServiceNotFoundException;
import model.services.*;

import java.util.Date;
import java.util.List;

public interface Model {

    Client getClient(long id) throws ClientNotFoundException, InvalidModelException;

    List<Client> getClients() throws InvalidModelException;

    void addClient(Client client) throws InvalidModelException;

    void removeClient(long id) throws ClientNotFoundException, InvalidModelException;

    List<ClientService<Internet>> getClientInternets(long id)
            throws ClientNotFoundException, InvalidModelException;

    List<ClientService<Phone>> getClientPhones(long id)
            throws ClientNotFoundException, InvalidModelException;

    List<ClientService<Television>> getClientTelevisions(long id)
            throws ClientNotFoundException, InvalidModelException;

    List<Condition<Internet>> getInternetHistory(long internetId)
            throws ServiceNotFoundException, InvalidModelException;

    void addInternetToClient(long clientId, Internet internet)
            throws ClientNotFoundException, InvalidModelException;

    void extendInternet(long internetId, Date date_end)
            throws ServiceNotFoundException, InvalidModelException;

//    List<Service> getClientServicesByType(long clientID, String serviceType)
//            throws ServiceNotFoundException, ClientNotFoundException, InvalidModelException;
//
//    List<Service> getServiceHistoryByType(long serviceID, String serviceType)
//            throws ServiceNotFoundException, InvalidModelException;
//
//    void addServiceToClient(long clientID, Service service) throws ClientNotFoundException, InvalidModelException;

}
