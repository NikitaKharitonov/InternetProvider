package controller;

import model.services.*;
import model.*;

import java.util.Map;
/**
 * Interface Controller
 * @author anteii
 * @version 0.1
 * */
public interface Controller {
    /**
     * Creates new user with such parameters {@code params}
     * @param params
     *          Map with declared specific keys
     * @throws FailedOperation
     *          If some troubles were happened
     * @return Created object
     * */
    User      createUser(Map<String, Object> params) throws FailedOperation;
    /**
     * Creates new tariff based on {@code service}
     * @param service
     *          Configured Service object
     * @throws FailedOperation
     *          If some troubles were happened
     * @return Created Service object
     * */
    Tariff   createTariff(Service service) throws FailedOperation;
    /**
     * Search user by his id
     * @param userID
     *          user id
     * @throws FailedOperation
     *          If some troubles were happened
     * @return Created Service object
     * */
    User      getUser(int userID) throws FailedOperation;
    /**
     * Return all supported services names
     * @return array of supported services names
     * */
    String[]  getServices();
    /**
     * Search tariff by its id and name of its service
     * @param serviceName
     *          Service name
     * @param tariffID
     *          Tariff id
     * @throws FailedOperation
     *          If some troubles were happened
     * @return created Tariff based on found tariff object
     * */
    Tariff    getTariff(String serviceName, int tariffID) throws FailedOperation;
    /**
     * Returns all tariffs of {@code serviceName}
     * @param serviceName
     *          Service name
     * @throws FailedOperation
     *          If some troubles were happened
     * @return Array of created Tariffs
     * */
    Tariff[] getAllTariffs(String serviceName) throws FailedOperation;
    /**
     * Set {@code tariff} to user by his id
     * @param userID
     *          User id
     * @param tariff
     *          Tariff object wich will be set up to user
     * @throws FailedOperation
     *          If some troubles were happened
     * @return tariff wich was set up to user
     * */
    Tariff   setTariffToUser(int userID, Tariff tariff) throws FailedOperation;
    /**
     * Change user data (E.g. name or email address)
     * @param userID
     *          User id
     * @param params
     *          Map with with declared parameters specific for User class
     * @throws FailedOperation
     *          If some troubles were happened
     * @return User object
     * */
    User      changeUserData(int userID, Map<String, Object> params) throws FailedOperation;
    /**
     * Change tariff parameters
     * @param tariffID
     *          Tariff id
     * @param tariff
     *          Tariff object wich will be changed
     * @throws FailedOperation
     *          If some troubles were happened
     * @return tariff wich was changed
     * */
    Tariff   changeTariff(int tariffID, Tariff tariff) throws FailedOperation;
    /**
     * Disable {@code serviceName} for user with such id
     * @param userID
     *          User id
     * @param serviceName
     *          Service name
     * @throws FailedOperation
     *          If some troubles were happened
     * @return tariff wich was disabled for user
     * */
    Tariff   removeTariffFromUser(int userID, String serviceName) throws FailedOperation;
    /**
     * Delete user
     * @param userID
     *          User id
     * @throws FailedOperation
     *          If some troubles were happened
     * @return User wich was deleted
     * */
    User      deleteUser(int userID) throws FailedOperation;
    /**
     * Delete tariff and instantly save changes in a storage
     * @param serviceName
     *          Service name
     * @param tariffID
     *          Tariff id
     * @return Tariff wich was deleted
     * */
    Tariff   deleteTariff(String serviceName, int tariffID) throws FailedOperation;
}
