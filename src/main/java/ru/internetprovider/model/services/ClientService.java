package ru.internetprovider.model.services;

import java.util.Date;

public class ClientService<T extends Service> {

    private final long id;
    private final Date activationDate;
    private final Condition<T> actualCondition;

    public ClientService(long id, Date activationDate, Condition<T> actualCondition) {
        this.id = id;
        this.activationDate = activationDate;
        this.actualCondition = actualCondition;
    }

    public long getId() {
        return id;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public Condition<T> getActualCondition() {
        return actualCondition;
    }

    @Override
    public String toString() {
        return "ClientService{" +
                "id=" + id +
                ", activationDate=" + activationDate +
                ", actualCondition=" + actualCondition.toString() +
                '}';
    }
}
