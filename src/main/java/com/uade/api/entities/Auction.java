package com.uade.api.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "subastas")
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "identificador")
    private int id;
    @Column(name = "fecha")
    private Date date;
    @Column(name = "hora")
    private Time time;
    @Column(name = "estado")
    private String status;
    @Column(name = "subastador")
    private int auctioner;
    @Column(name = "ubicacion")
    private String location;
    @Column(name = "capacidadAsistentes")
    private int capacity;
    @Column(name = "tieneDeposito")
    private String hasDeposit;
    @Column(name = "seguridadPropia")
    private String hasSecurity;
    @Column(name = "categoria")
    private String category;

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAuctioner() {
        return auctioner;
    }

    public void setAuctioner(int auctioner) {
        this.auctioner = auctioner;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getHasDeposit() {
        return hasDeposit;
    }

    public void setHasDeposit(String hasDeposit) {
        this.hasDeposit = hasDeposit;
    }

    public String getHasSecurity() {
        return hasSecurity;
    }

    public void setHasSecurity(String hasSecurity) {
        this.hasSecurity = hasSecurity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
