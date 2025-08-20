package com.group3.inventhor.controller;

import com.group3.inventhor.dto.SellingHistoryDTO;
import com.group3.inventhor.dto.SellingHistoryYearFilterDTO;
import com.group3.inventhor.service.SellingHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @Author Steewen Dennis Chanavi Holden.
 * Controller for getting all sales history and get sales history by ID
 */

@RestController
@RequestMapping("/sellinghistory")
@RequiredArgsConstructor
public class SellingHistoryController {

    private final SellingHistoryService sellingHistoryService;

    /**
     *Get all selling history
     */
    @GetMapping
    public ResponseEntity<List<SellingHistoryDTO>> getAllSellingHistory() {
        return ResponseEntity.ok(sellingHistoryService.getAllSellingHistory());
    }

    /**
     *Get selling history by product number
     *
     * @param productnr the product number to filter selling history
     * @return a list of SellingHistoryDTO objects
     */
    @GetMapping("/yearly/{productnr}")
    public ResponseEntity<SellingHistoryYearFilterDTO> getSellingHistoryByProductnr(@PathVariable Integer productnr) {
        return ResponseEntity.ok(sellingHistoryService.getSellingHistoryByProductnr(productnr));
    }

    /**
     *Get selling history by ID
     */
    @GetMapping("/{sellinghistorynr}")
    public ResponseEntity<SellingHistoryDTO> getSellingHistoryById(@PathVariable Integer sellinghistorynr) {
        return ResponseEntity.ok(sellingHistoryService.getSellingHistoryById(sellinghistorynr));
    }

}
