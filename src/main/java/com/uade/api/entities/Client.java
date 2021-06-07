package com.uade.api.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "clientes")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "identificador")
    private int id;
    @Column(name = "numeroPais")
    private int countryNumber;
    @Column(name = "admitido")
    private String clientStatus;
    @Column(name = "categoria")
    private String category;
    @Column(name = "verificador")
    private int verifier;

    public int getCountryNumber() {
        return countryNumber;
    }

    public void setCountryNumber(int countryNumber) {
        this.countryNumber = countryNumber;
    }

    public String getClientStatus() {
        return clientStatus;
    }

    public void setClientStatus(String clientStatus) {
        this.clientStatus = clientStatus;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getVerifier() {
        return verifier;
    }

    public void setVerifier(int verifier) {
        this.verifier = verifier;
    }
}
