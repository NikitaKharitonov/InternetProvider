package ru.internetprovider.model.dao;

import ru.internetprovider.model.services.Television;
import ru.internetprovider.model.services.TelevisionSpecification;

/**
 * The implementation of the Data Access Object pattern for Television services.
 */
public interface TelevisionDao extends ServiceDao<TelevisionSpecification, Television> {
}
