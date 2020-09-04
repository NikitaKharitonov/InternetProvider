package ru.internetprovider.model.dao;

import ru.internetprovider.model.services.Television;
import ru.internetprovider.model.services.TelevisionState;

/**
 * The implementation of the Data Access Object pattern for Television services.
 */
public interface TelevisionDao extends ServiceDao<TelevisionState, Television> {
}
