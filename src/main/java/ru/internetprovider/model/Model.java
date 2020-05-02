package ru.internetprovider.model;

import ru.internetprovider.model.exceptions.ClientNotFoundException;
import ru.internetprovider.model.exceptions.InvalidModelException;
import ru.internetprovider.model.exceptions.ServiceNotFoundException;
import ru.internetprovider.model.services.*;

import java.sql.SQLException;
import java.util.List;

public interface Model {

    /*
    Client
    * */

    Client getClient(long id) throws ClientNotFoundException, InvalidModelException, SQLException;

    List<Client> getClientList() throws InvalidModelException, SQLException;

    void updateClient(long id, Client client) throws SQLException;

    void addClient(Client client) throws InvalidModelException, SQLException;

    void removeClient(long id) throws ClientNotFoundException, InvalidModelException, SQLException;

    /*
    Internet
    * */

    ClientService<Internet> getInternet(long internetId) throws ClientNotFoundException, SQLException, ServiceNotFoundException;

    List<ClientService<Internet>> getInternetList(long clientId)
            throws ClientNotFoundException, InvalidModelException, SQLException, ServiceNotFoundException;

    void updateInternet(long internetId, Internet internet) throws SQLException;

    void addInternet(long clientId, Internet internet) throws ClientNotFoundException, SQLException;

    void removeInternet(long internetId) throws SQLException, ClientNotFoundException;

    List<Internet> getInternetHistory(long internetId)
            throws ServiceNotFoundException, InvalidModelException, SQLException;

    /*
    Phone
    * */

    ClientService<Phone> getPhone(long phoneId) throws SQLException, ClientNotFoundException, ServiceNotFoundException;

    List<ClientService<Phone>> getPhoneList(long clientId)
            throws ClientNotFoundException, InvalidModelException, SQLException, ServiceNotFoundException;

    void updatePhone(long phoneId, Phone phone) throws SQLException;

    void addPhone(long clientId, Phone phone) throws SQLException, ClientNotFoundException;

    void removePhone(long phoneId) throws SQLException;

    List<Phone> getPhoneHistory(long phoneId) throws ServiceNotFoundException, InvalidModelException, SQLException;

    /*
    Television
    * */

    ClientService<Television> getTelevision(long televisionId) throws ClientNotFoundException, SQLException, ServiceNotFoundException;

    List<ClientService<Television>> getTelevisionList(long clientId)
            throws ClientNotFoundException, InvalidModelException, SQLException, ServiceNotFoundException;

    void updateTelevision(long televisionId, Television television) throws SQLException;

    void addTelevision(long clientId, Television television) throws SQLException, ClientNotFoundException;

    void removeTelevision(long televisionId) throws SQLException;

    List<Television> getTelevisionHistory(long televisionId) throws ServiceNotFoundException, InvalidModelException, SQLException;

}
