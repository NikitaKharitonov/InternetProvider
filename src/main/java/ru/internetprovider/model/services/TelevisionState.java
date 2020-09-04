package ru.internetprovider.model.services;

import javax.persistence.*;
import java.util.Date;

/**
 * A temporal state of a Television service.
 */
@Entity
@Table(name = "television_state")
public class TelevisionState implements ServiceState {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The ID of the Television service that has this state.
     */
    @Column(name = "television_id")
    private int televisionId;

    /**
     * The date when this state was assigned to its Television service.
     */
    @Column(name = "begin_date")
    private Date beginDate;

    /**
     * The date when this state became no longer active (e. g. when
     * the Television service's status was switched from ACTIVE to SUSPENDED or
     * DELETED or when a new state was assigned to the service (i. e.
     * the service was updated)).
     */
    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "channels_count")
    private int channelsCount;

    public TelevisionState(Date beginDate, Date endDate, int channelsCount) {
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.channelsCount = channelsCount;
    }

    public TelevisionState() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTelevisionId() {
        return televisionId;
    }

    public void setTelevisionId(int televisionId) {
        this.televisionId = televisionId;
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

    public int getChannelsCount() {
        return channelsCount;
    }

    public void setChannelsCount(int channelsCount) {
        this.channelsCount = channelsCount;
    }

    @Override
    public String toString() {
        return "Television{" +
                "id=" + id +
                ", televisionId=" + televisionId +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", channelsCount=" + channelsCount +
                '}';
    }
}
