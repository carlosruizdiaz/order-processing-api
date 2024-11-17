package com.challenge.process.applicaton.service;

import com.challenge.process.applicaton.mapper.OrderMapper;
import com.challenge.process.domain.model.Order;
import com.challenge.process.domain.port.OrderProcessingPort;
import com.challenge.process.domain.port.OrderRepositoryPort;
import com.challenge.process.web.dto.OrderDTO;
import com.challenge.process.web.dto.OrderItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private final OrderProcessingPort orderProcessingPort;

    @Autowired
    private final OrderRepositoryPort orderRepositoryPort;

    @Autowired
    private OrderMapper orderMapper;

    public OrderService(OrderProcessingPort orderProcessingPort, OrderRepositoryPort orderRepositoryPort, OrderMapper orderMapper) {
        this.orderProcessingPort = orderProcessingPort;
        this.orderRepositoryPort = orderRepositoryPort;
        this.orderMapper = orderMapper;
    }

    public void processOrder(OrderDTO orderDto) {
        validateOrderId(orderDto.getOrderId());
        validateCustomerId(orderDto.getCustomerId());
        validateOrderAmount(orderDto.getOrderAmount());
        validateOrderItems(orderDto.getOrderItems());

        Order order = orderMapper.parseDtoToModel(orderDto);

        orderProcessingPort.processOrder(order);
        orderRepositoryPort.save(order);
    }

    /**
     * Validar que el orderId no sea nulo o vacío
     */
    private void validateOrderId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty.");
        }
    }

    /**
     * Validar que el customerId no sea nulo o vacío
     */
    private void validateCustomerId(String customerId ) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty.");
        }
    }

    /**
     * Validar que el monto total del pedido sea positivo
     */
    private void validateOrderAmount(double orderAmount) {
        if (orderAmount <= 0) {
            throw new IllegalArgumentException("Order amount must be greater than zero.");
        }
    }

    /**
     * Validar que la lista de orderItems no sea nula ni vacía
     */
    private void validateOrderItems(List<OrderItemDTO> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item.");
        }
    }
}
