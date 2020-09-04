package ru.internetprovider.model.services;

import javax.persistence.*;
import java.util.Date;

/**
 * A temporal state of a Phone service.
 */
@Entity
@Table(name = "phone_state")
public class PhoneState implements ServiceState {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The ID of the Phone service that has this state.
     */
    @Column(name = "phone_id")
    private int phoneId;

    /**
     * The date when this state was assigned to its Phone service.
     */
    @Column(name = "begin_date")
    private Date beginDate;

    /**
     * The date when this state became no longer active (e. g. when
     * the Phone service's status was switched from ACTIVE to SUSPENDED or
     * DELETED or when a new state was assigned to the service (i. e.
     * the service was updated)).
     */
    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "mins_count")
    private int minsCount;

    @Column(name = "sms_count")
    private int smsCount;

    public PhoneState(Date beginDate, Date endDate, int minsCount, int smsCount) {
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.minsCount = minsCount;
        this.smsCount = smsCount;
    }

    public PhoneState() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(int phoneId) {
        this.phoneId = phoneId;
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

    public int getMinsCount() {
        return minsCount;
    }

    public void setMinsCount(int minsCount) {
        this.minsCount = minsCount;
    }

    public int getSmsCount() {
        return smsCount;
    }

    public void setSmsCount(int smsCount) {
        this.smsCount = smsCount;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", phoneId=" + phoneId +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", minsCount=" + minsCount +
                ", smsCount=" + smsCount +
                '}';
    }
}
