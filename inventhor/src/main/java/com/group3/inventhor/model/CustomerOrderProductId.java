package com.group3.inventhor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author Steewen Dennis Chanavi Holden
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrderProductId implements Serializable {
    private Integer ordernr;
    private Integer productnr;
    private Integer warehousenr;
}