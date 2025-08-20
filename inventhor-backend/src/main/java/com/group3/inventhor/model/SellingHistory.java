package com.group3.inventhor.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * @Author Steewen Dennis Chanavi Holden
 */

@Entity
@Getter
@Setter
@Table(name = "sellinghistory", schema = "inventhor")
public class SellingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sellinghistorynr;

    @Column(name = "productnr")
    private Integer productnr;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "saledate")
    private LocalDateTime saledate;
}
