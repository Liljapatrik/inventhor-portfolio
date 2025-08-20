package com.group3.inventhor.service;

import com.group3.inventhor.dto.PriceHistoryDTO;
import com.group3.inventhor.dto.PriceHistoryYearFilterDTO;
import com.group3.inventhor.mapper.PriceHistoryMapper;
import com.group3.inventhor.model.PriceHistory;
import com.group3.inventhor.repository.PriceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author Steewen Dennis Chanavi Holden
 *
 * The PriceHistoryService class provides a method to get the history for all prices for the Inventhor application.
 * Includes a method for getting all price history.
 */

@Service
@RequiredArgsConstructor
public class PriceHistoryService {
    private final PriceHistoryRepository priceHistoryRepository;
    private final PriceHistoryMapper priceHistoryMapper;

    /**
     *
     * Get all price history
     */
    public List<PriceHistoryDTO> getAllPriceHistory() {
        List<PriceHistory> priceHistories = priceHistoryRepository.findAll();
        return priceHistoryMapper.toPriceHistoryDTOs(priceHistories);
    }

    /**
     * Get price history for a specific product
     * It will show records og average price for each month for the current year and the previous year.
     *
     * @param productnr the product number to filter price history
     * @return a list of PriceHistoryDTO objects
     */
    public PriceHistoryYearFilterDTO getYearlyPriceHistoryByProductnr(Integer productnr) {
        int currentYear = java.time.Year.now().getValue();
        int pastYear = currentYear - 1;

        List<PriceHistory> priceHistories = priceHistoryRepository.findByProductnr(productnr);

        Map<String, Double> currentYearMonths = priceHistories.stream()
                .filter(ph -> ph.getSetdate().getYear() == currentYear)
                .filter(ph -> ph.getBuyprice() != null)
                .collect(Collectors.groupingBy(
                        ph -> ph.getSetdate().getMonth().name(),
                        Collectors.averagingDouble(ph -> ph.getBuyprice().doubleValue())
                ));

        Map<String, Double> pastYearMonths = priceHistories.stream()
                .filter(ph -> ph.getSetdate().getYear() == pastYear)
                .filter(ph -> ph.getBuyprice() != null)
                .collect(Collectors.groupingBy(
                        ph -> ph.getSetdate().getMonth().name(),
                        Collectors.averagingDouble(ph -> ph.getBuyprice().doubleValue())
                ));

        PriceHistoryYearFilterDTO dto = new PriceHistoryYearFilterDTO();
        dto.setCurrentYear(currentYear);
        dto.setCurrentYearMonths(currentYearMonths.isEmpty() ? Map.of() : currentYearMonths);
        dto.setPastYear(pastYear);
        dto.setPastYearMonths(pastYearMonths.isEmpty() ? Map.of() : pastYearMonths);

        return dto;
    }

    /**
     *
     * Get price history by ID
     */
    public PriceHistoryDTO getPriceHistoryById(Integer pricehistorynr) {
        PriceHistory entity = priceHistoryRepository.findById(pricehistorynr)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("PriceHistory not found with id: " + pricehistorynr));
        return priceHistoryMapper.toPriceHistoryDTO(entity);
    }
}
