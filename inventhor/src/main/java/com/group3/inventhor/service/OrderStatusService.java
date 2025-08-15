package com.group3.inventhor.service;

import com.group3.inventhor.dto.OrderStatusDTO;
import com.group3.inventhor.mapper.OrderStatusMapper;
import com.group3.inventhor.model.OrderStatus;
import com.group3.inventhor.repository.OrderStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author Tatiana Fløisbonn
 *
 * The OrderStatusService class provides methods to manage order statuses in the Inventhor application.
 * It includes methods to get all order statuses.
 *
 * @Service indicates that this is a service class that contains business logic and interacts with the data access layer.
 * @RequiredArgsConstructor generates a constructor with all required dependencies.
 *
 * It will be possible only to retrieve information about order statuses.
 * Create, update, and delete methods are not available since order statuses are predefined and should not be modified by users.
 *
 */
@Service
@RequiredArgsConstructor
public class OrderStatusService {

    private final OrderStatusRepository orderStatusRepository;
    private final OrderStatusMapper orderStatusMapper;

    /**
     * Get all order statuses.
     *
     * @return List of OrderStatus containing all order statuses.
     */
    public List<OrderStatusDTO> getAllOrderStatuses() {
        // Retrieve all order statuses from the repository and convert them to DTOs
        List<OrderStatus> orderStatuses = orderStatusRepository.findAll();
        return orderStatusMapper.toOrderStatusDTOs(orderStatuses);
    }

}
