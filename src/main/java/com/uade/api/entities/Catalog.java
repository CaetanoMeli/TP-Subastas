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
import javax.persistence.Table;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "catalogos")
public class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identificador")
    private int id;
    @Column(name = "descripcion")
    private String description;
    @Column(name = "responsable")
    private int owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subasta", referencedColumnName = "identificador")
    private Auction auction;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "catalog", orphanRemoval = true)
    private List<CatalogItem> catalogItems;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "catalog", orphanRemoval = true)
    private List<Bid> bids;
}