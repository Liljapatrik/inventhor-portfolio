package com.group3.inventhor.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author Steewen Dennis Chanavi Holden
 */

@Entity
@Getter
@Setter
@Table(name = "pricehistory", schema = "inventhor")
public class PriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pricehistorynr;

    @Column(name = "productnr")
    private Integer productnr;

    @Column(name = "sellprice")
    private BigDecimal sellprice;

    @Column(name = "buyprice")
    private BigDecimal buyprice;

    @Column(name = "setdate")
    private LocalDateTime setdate;
}
