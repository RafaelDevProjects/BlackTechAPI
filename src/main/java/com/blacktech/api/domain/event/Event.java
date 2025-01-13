package com.blacktech.api.domain.event;

import com.blacktech.api.domain.address.Address;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Table(name = "event")
@Entity
public class Event {
    @Id
    @GeneratedValue
    private UUID id;

    private  String title;
    private String description;
    private String imgUrl;
    private String eventUrl;
    private Boolean remote;
    private Date date;

    @OneToOne(mappedBy = "event", cascade = CascadeType.ALL)
    private Address address;

    public Event() {
    }

    public Event(UUID id, String title, String description, String imgUrl, String eventUrl, Boolean remote, Date date) {        this.id = id;
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.eventUrl = eventUrl;
        this.remote = remote;
        this.date = date;
    }

    public Event(UUID id, String title, String description, String imgUrl, String eventUrl, Boolean remote, Date date, Address address) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.eventUrl = eventUrl;
        this.remote = remote;
        this.date = date;
        this.address = address;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    public void setRemote(Boolean remote) {
        this.remote = remote;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public Boolean getRemote() {
        return remote;
    }

    public Date getDate() {
        return date;
    }

    public Address getAddress() {
        return address;
    }
}
