package com.group3.inventhor.controller;

import com.group3.inventhor.dto.WarehouseOrderProductDTO;
import com.group3.inventhor.service.WarehouseOrderProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Nils Patrik Lilja
 */
@CrossOrigin(origins = {"http://localhost:3000", "http://10.0.2.2:3000"})
@RestController
@RequestMapping("/warehouse-order-product")
@Tag(name="Warehouseorderproduct Controller", description = "API for managing warehouseorderproduct")
@RequiredArgsConstructor
public class WarehouseOrderProductController {

    private final WarehouseOrderProductService warehouseOrderProductService;

    /**
     * GET warehouse order product by order ID and product ID
     * Handles HTTP GET request to retrieve a specific product within a warehouse order.
     * param ordernr   the ID of the warehouse order (path variable)
     * param productnr the ID of the product within the order (path variable)
     * return ResponseEntity with WarehouseOrderProductDTO and HTTP 200 OK if found,
     * or HTTP 404 NOT FOUND if the product or order does not exist.
     */
    @GetMapping("/{ordernr}/{productnr}")
    public ResponseEntity<WarehouseOrderProductDTO> getWarehouseOrderProduct(
            @PathVariable Integer ordernr,
            @PathVariable Integer productnr) {
        WarehouseOrderProductDTO warehouseOrderProductDTO = warehouseOrderProductService.getProductByOrder(ordernr, productnr);
        if (warehouseOrderProductDTO != null) {
            return new ResponseEntity<>(warehouseOrderProductDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * GET all products for a given warehouse order
     * Handles HTTP GET request to retrieve all products associated with a specific warehouse order.
     * param ordernr the ID of the warehouse order (path variable)
     * return ResponseEntity with a list of WarehouseOrderProductDTO and HTTP 200 OK if any products exist,
     * or HTTP 204 NO CONTENT if there are no products for the given order.
     */
    @GetMapping("{ordernr}")
    public ResponseEntity<List<WarehouseOrderProductDTO>> getOrdernr(
        @PathVariable Integer ordernr) {
    List<WarehouseOrderProductDTO> orders = warehouseOrderProductService.getProductByOrder(ordernr);
            if (orders.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(orders, HttpStatus.OK);

    }
}
