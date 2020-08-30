package ru.internetprovider.model.services;

import javax.persistence.*;
import java.util.Date;

/**
 * A temporal specification of a Television service,
 * it has the mutable properties of a Television service.
 */

@Entity
@Table(name = "television_specification")
public class TelevisionSpecification implements ServiceSpecification {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "television_id")
    private int televisionId;
    @Column(name = "begin_date")
    private Date beginDate;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "channels_count")
    private int channelsCount;

    public TelevisionSpecification(Date beginDate, Date endDate, int channelsCount) {
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.channelsCount = channelsCount;
    }

    public TelevisionSpecification() {
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
