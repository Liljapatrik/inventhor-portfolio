package com.group3.inventhor.controller;

import com.group3.inventhor.dto.PriceHistoryDTO;
import com.group3.inventhor.dto.PriceHistoryYearFilterDTO;
import com.group3.inventhor.service.PriceHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @Author Steewen Dennis Chanavi Holden.
 * Controller for getting all price history and get price history by ID
 */

@RestController
@RequestMapping("/pricehistory")
@RequiredArgsConstructor
public class PriceHistoryController {

    private final PriceHistoryService priceHistoryService;

    /**
     *Get all price history
     */
    @GetMapping
    public ResponseEntity<List<PriceHistoryDTO>> getAllPriceHistory() {
        return ResponseEntity.ok(priceHistoryService.getAllPriceHistory());
    }

    /**
     * Get avarage price history for specific product for each month for the current year and the previous year.
     *
     *
     * @Operation provides a summary and description for the endpoint
     * @GetMapping maps the HTTP GET request to the specified path
     *
     * @param productnr the product number to filter price history
     * @return a list of PriceHistoryYearFilterDTO objects containing the average prices for each month of the current and past year
     */
    @Operation(summary = "Get average price history for a specific product",
            description = "Returns the average prices for each month of the current year and the previous year for the specified product number.")
    @GetMapping("/yearly/{productnr}")
    public PriceHistoryYearFilterDTO getYearlyPriceHistoryByProductnr(@PathVariable Integer productnr) {
        return priceHistoryService.getYearlyPriceHistoryByProductnr(productnr);
    }


    /**
     *Get price history by ID
     */
    @GetMapping("/{pricehistorynr}")
    public ResponseEntity<PriceHistoryDTO> getPriceHistoryById(@PathVariable Integer pricehistorynr) {
        return ResponseEntity.ok(priceHistoryService.getPriceHistoryById(pricehistorynr));
    }

}
