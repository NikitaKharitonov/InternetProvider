package ru.internetprovider.model.dao;

import ru.internetprovider.model.services.Phone;
import ru.internetprovider.model.services.PhoneSpecification;

/**
 * The implementation of the Data Access Object pattern for Phone services.
 */
public interface PhoneDao extends ServiceDao<PhoneSpecification, Phone> {
}
