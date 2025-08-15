package com.group3.inventhor.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Furo Muktar Eshetu
 * the warehouse class represents a warehouse entity in the inventhor application
*/


@Entity
@Getter
@Setter
@Table(name= "warehouse", schema = "inventhor")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int warehousenr;
    private String name;

    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "addressnr")
    private Address address;
}

