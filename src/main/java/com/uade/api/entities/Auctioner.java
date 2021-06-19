package com.uade.api.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "subastadores")
public class Auctioner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identificador")
    private int id;
    @Column(name = "matricula")
    private String drivingLicense;
    @Column(name = "region")
    private String region;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "auctioner", orphanRemoval = true)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "auctioner", orphanRemoval = true)
    private List<Auction> auction;

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
