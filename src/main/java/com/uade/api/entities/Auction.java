package com.uade.api.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Time;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "subastas")
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identificador")
    private int id;
    @Column(name = "fecha")
    private Date date;
    @Column(name = "hora")
    private Time time;
    @Column(name = "estado")
    private String status;
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
    @Column(name = "moneda")
    private String currencyType;
    @Column(name = "image")
    private String image;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "subastador", referencedColumnName = "identificador")
    private Auctioner auctioner;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "auction")
    private List<Catalog> catalogList;
}
