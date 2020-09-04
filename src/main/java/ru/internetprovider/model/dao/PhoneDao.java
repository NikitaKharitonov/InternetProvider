package ru.internetprovider.model.dao;

import ru.internetprovider.model.services.Phone;
import ru.internetprovider.model.services.PhoneState;

/**
 * The implementation of the Data Access Object pattern for Phone services.
 */
public interface PhoneDao extends ServiceDao<PhoneState, Phone> {
}
