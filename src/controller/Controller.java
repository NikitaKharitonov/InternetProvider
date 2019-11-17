package controller;

import model.services.*;
import model.*;

import java.util.Map;

public interface Controller {

    User      createUser(Map<String, Object> params) throws FailedOperation;
    Service   createTariff(Service service) throws FailedOperation;

    User      getUser(Integer userID);
    String[]  getServices();
    Tariff getTariff(String serviceName, Integer tariffID) throws FailedOperation;
    Service[] getAllTariffs(String serviceName);

    Service   setTariffToUser(Integer userID, Tariff tariff) throws FailedOperation;

    User      changeUserData(Integer userID, Map<String, Object> params) throws FailedOperation;
    Service   changeUserTariff(Integer userID, Tariff tariff) throws FailedOperation;
    Service   changeTariff(Integer tariffID, Tariff service) throws FailedOperation;

    Service   removeTariffFromUser(Integer userID, String serviceName) throws FailedOperation;

    User      deleteUser(Integer userID);
    Service   deleteTariff(String serviceName, Integer tariffID);
}
