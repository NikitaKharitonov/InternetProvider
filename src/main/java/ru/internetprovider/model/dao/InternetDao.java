package ru.internetprovider.model.dao;

import ru.internetprovider.model.services.Internet;
import ru.internetprovider.model.services.InternetState;

/**
 * The implementation of the Data Access Object pattern for Internet services.
 */
public interface InternetDao extends ServiceDao<InternetState, Internet> {
}
