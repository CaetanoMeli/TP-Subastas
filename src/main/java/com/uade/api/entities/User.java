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
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "personas")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identificador")
    private int id;
    @Column(name = "documento")
    private String dni;
    @Column(name = "nombre")
    private String firstName;
    @Column(name = "apellido")
    private String lastName;
    @Column(name = "direccion")
    private String address;
    @Column(name = "email")
    private String email;
    @Column(name = "telefono")
    private String phone;
    @Column(name = "codigo")
    private String code;
    @Column(name = "contraseña")
    private String password;
    @Column(name = "estado")
    private String status;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Client client;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identificador")
    private Owner owner;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identificador")
    private Auctioner auctioner;
}
