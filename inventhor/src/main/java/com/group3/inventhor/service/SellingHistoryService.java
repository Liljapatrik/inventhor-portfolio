package com.group3.inventhor.service;

import com.group3.inventhor.dto.SellingHistoryDTO;
import com.group3.inventhor.dto.SellingHistoryYearFilterDTO;
import com.group3.inventhor.mapper.SellingHistoryMapper;
import com.group3.inventhor.model.SellingHistory;
import com.group3.inventhor.repository.SellingHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author Steewen Dennis Chanavi Holden
 *
 * The SellingHistoryService class provides a method to get the history for all sales for the Inventhor application.
 * Includes a method for getting all sales history.
 */

@Service
@RequiredArgsConstructor
public class SellingHistoryService {
    private final SellingHistoryRepository sellingHistoryRepository;
    private final SellingHistoryMapper sellingHistoryMapper;

    /**
     *
     * Get all selling history
     */
    public List<SellingHistoryDTO> getAllSellingHistory() {
        List<SellingHistory> sellingHistories = sellingHistoryRepository.findAll();
        return sellingHistoryMapper.toSellingHistoryDTOs(sellingHistories);
    }

    /**
     * Get selling history by productnr
     *
     * @param productnr the product number to filter selling history
     * @return a list of SellingHistoryDTO objects
     */
    public SellingHistoryYearFilterDTO getSellingHistoryByProductnr(Integer productnr) {
        List<SellingHistory> sellingHistories = sellingHistoryRepository.findByProductnr(productnr);

        int currentYear = java.time.Year.now().getValue();
        int pastYear = currentYear - 1;
        int currentMonthValue = java.time.LocalDate.now().getMonthValue();

        // For current year: months up to the last completed month
        int currentYearMonthsToInclude = currentMonthValue - 1;
        // For past year: all 12 months
        int pastYearMonthsToInclude = 12;

        List<String> currentYearMonthNames = new ArrayList<>();
        for (int m = 1; m <= currentYearMonthsToInclude; m++) {
            currentYearMonthNames.add(Month.of(m).name());
        }
        List<String> pastYearMonthNames = new ArrayList<>();
        for (int m = 1; m <= pastYearMonthsToInclude; m++) {
            pastYearMonthNames.add(Month.of(m).name());
        }

        List<Double> currentYearValues = buildMonthValues(sellingHistories, currentYear, currentYearMonthsToInclude);
        List<Double> pastYearValues = buildMonthValues(sellingHistories, pastYear, pastYearMonthsToInclude);

        Map<String, Double> currentYearMonths = new LinkedHashMap<>();
        for (int i = 0; i < currentYearValues.size(); i++) {
            currentYearMonths.put(currentYearMonthNames.get(i), currentYearValues.get(i));
        }
        Map<String, Double> pastYearMonths = new LinkedHashMap<>();
        for (int i = 0; i < pastYearValues.size(); i++) {
            pastYearMonths.put(pastYearMonthNames.get(i), pastYearValues.get(i));
        }

        SellingHistoryYearFilterDTO filterDTO = new SellingHistoryYearFilterDTO();
        filterDTO.setCurrentYear(currentYear);
        filterDTO.setCurrentYearMonths(currentYearMonths);
        filterDTO.setPastYear(pastYear);
        filterDTO.setPastYearMonths(pastYearMonths);

        return filterDTO;
    }

    private List<Double> buildMonthValues(List<SellingHistory> sellingHistories, int year, int monthsToInclude) {
        List<Double> values = new ArrayList<>();
        for (int m = 1; m <= monthsToInclude; m++) {
            int month = m;
            int sum = sellingHistories.stream()
                    .filter(s -> s.getSaledate().getYear() == year && s.getSaledate().getMonthValue() == month)
                    .mapToInt(SellingHistory::getQuantity)
                    .sum();
            values.add((double) sum);
        }
        return values;
    }

    /**
     *
     * Get selling history by ID
     */
    public SellingHistoryDTO getSellingHistoryById(Integer sellinghistorynr) {
        SellingHistory entity = sellingHistoryRepository.findById(sellinghistorynr)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("SellingHistory not found with id: " + sellinghistorynr));
        return sellingHistoryMapper.toSellingHistoryDTO(entity);
    }
}
