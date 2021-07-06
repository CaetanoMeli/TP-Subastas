package com.uade.api.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "empleados")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identificador")
    private int id;
    @Column(name = "cargo")
    private String position;
    @Column(name = "sector")
    private int sector;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee", orphanRemoval = true)
    private List<Product> products;
}
