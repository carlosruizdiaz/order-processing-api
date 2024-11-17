package com.challenge.process.application.service;

import com.challenge.process.applicaton.mapper.OrderMapper;
import com.challenge.process.applicaton.service.OrderService;
import com.challenge.process.domain.model.Order;
import com.challenge.process.domain.model.OrderItem;
import com.challenge.process.domain.port.OrderProcessingPort;
import com.challenge.process.domain.port.OrderRepositoryPort;
import com.challenge.process.web.dto.OrderDTO;
import com.challenge.process.web.dto.OrderItemDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

public class OrderServiceTest {

    @Mock
    private OrderProcessingPort orderProcessingPort;

    @Mock
    private OrderRepositoryPort orderRepositoryPort;

    @Mock
    private OrderMapper orderMapper;

    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderProcessingPort, orderRepositoryPort, orderMapper);
    }

    @Test
    public void testProcessOrder_Success() {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setItemId("1");
        orderItemDTO.setQuantity(2);
        orderItemDTO.setPrice(100.0);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId("123");
        orderDTO.setCustomerId("456");
        orderDTO.setOrderAmount(200.0);
        orderDTO.setOrderStatus("PENDING");
        orderDTO.setOrderItems(List.of(orderItemDTO));

        Order order = new Order();
        order.setOrderId("123");
        order.setCustomerId("456");
        order.setOrderAmount(200.0);
        order.setOrderStatus("PENDING");

        OrderItem orderItem = new OrderItem();
        orderItem.setItemId("1");
        orderItem.setQuantity(2);
        orderItem.setPrice(100.0);

        when(orderMapper.parseDtoToModel(orderDTO)).thenReturn(order);

        orderService.processOrder(orderDTO);

        verify(orderMapper).parseDtoToModel(orderDTO);
        verify(orderProcessingPort).processOrder(order);
        verify(orderRepositoryPort).save(order);
    }

    @Test
    public void testProcessOrder_InvalidOrderId() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.processOrder(orderDTO);
        });

        assertEquals("Order ID cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testProcessOrder_InvalidCustomerId() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId("123");
        orderDTO.setCustomerId("");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.processOrder(orderDTO);
        });

        assertEquals("Customer ID cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testProcessOrder_InvalidOrderAmount() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId("123");
        orderDTO.setCustomerId("456");
        orderDTO.setOrderAmount(0.0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.processOrder(orderDTO);
        });

        assertEquals("Order amount must be greater than zero.", exception.getMessage());
    }

    @Test
    public void testProcessOrder_InvalidOrderItems() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId("123");
        orderDTO.setCustomerId("456");
        orderDTO.setOrderAmount(200.0);
        orderDTO.setOrderItems(List.of());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.processOrder(orderDTO);
        });

        assertEquals("Order must contain at least one item.", exception.getMessage());
    }
}

