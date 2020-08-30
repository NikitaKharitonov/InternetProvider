package ru.internetprovider.model.services;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * A specification of an Internet service.
 * Contains the Internet parameters that may be changed by a user.
 */

@Entity
@Table(name = "internet_specification")
public class InternetSpecification implements ServiceSpecification {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The ID of the Internet service that has this specification.
     */
    @Column(name = "internet_id")
    private int internetId;

    /**
     * The date when this specification was assigned to its Internet service.
     */
    @Column(name = "begin_date")
    private Date beginDate;

    /**
     * The date when this specification became no longer active (e. g. when
     * the Internet service's status was switched from ACTIVE to SUSPENDED or
     * DELETED or when a new specification was assigned to the service (i. e.
     * the service was updated)).
     */
    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "speed")
    private int speed;

    @Column(name = "antivirus")
    private boolean antivirus;

    @Enumerated(EnumType.STRING)
    @Column(name = "connection_type", columnDefinition = "connection_type")
    @Type(type = "ru.internetprovider.model.services.PostgreSQLEnumType")
    private ConnectionType connectionType;

    public InternetSpecification(Date beginDate, Date endDate, int speed, boolean antivirus, ConnectionType connectionType) {
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.speed = speed;
        this.antivirus = antivirus;
        this.connectionType = connectionType;
    }

    public InternetSpecification() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInternetId() {
        return internetId;
    }

    public void setInternetId(int internetId) {
        this.internetId = internetId;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isAntivirus() {
        return antivirus;
    }

    public void setAntivirus(boolean antivirus) {
        this.antivirus = antivirus;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    @Override
    public String toString() {
        return "Internet{" +
                "id=" + id +
                ", internetId=" + internetId +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", speed=" + speed +
                ", antivirus=" + antivirus +
                ", connectionType=" + connectionType +
                '}';
    }
}
