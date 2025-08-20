package com.group3.inventhor.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Nils Patrik Lilja
 *
 * The Supplier class represents a supplier entity in the Inventhor application.
 */
@Entity
@Getter
@Setter
@Table(name = "supplier", schema = "inventhor") // specify the name of the table and schema
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer suppliernr;
    private String name;
    private String contactperson;
    private String email;
    private String phone;
    private String website;
    private String notes;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressnr")
    private Address address;

}
