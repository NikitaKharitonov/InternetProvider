package model.services;

import java.util.Date;

public class ActivatedService {

    private final long serviceID;
    private final String type;
    private final String activationDate;

    public ActivatedService(long serviceID, String type) {
        this.serviceID = serviceID;
        this.type = type;
        activationDate = new Date().toString().replaceAll(" ", "_");
    }

    public ActivatedService(long serviceID, String type, String activationDate) {
        this.serviceID = serviceID;
        this.type = type;
        this.activationDate = activationDate;
    }

    public long getServiceID() {
        return serviceID;
    }

    public String getType() {
        return type;
    }

    public String getActivationDate() {
        return activationDate;
    }
}
