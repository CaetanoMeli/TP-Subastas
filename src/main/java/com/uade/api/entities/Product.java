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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "productos")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identificador")
    private int id;
    @Column(name = "fecha")
    private Date date;
    @Column(name = "disponible")
    private String available;
    @Column(name = "descripcion_catalogo")
    private String catalogDescription;
    @Column(name = "descripcion_completa")
    private String completeDescription;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    private List<CatalogItem> catalogItems;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    private List<Picture> pictures;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "duenio", referencedColumnName = "identificador")
    private Owner owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revisor", referencedColumnName = "identificador")
    private Employee employee;
}