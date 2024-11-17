package com.challenge.process.application.mapper;

import com.challenge.process.applicaton.mapper.OrderMapper;
import com.challenge.process.domain.model.Order;
import com.challenge.process.domain.model.OrderItem;
import com.challenge.process.web.dto.OrderDTO;
import com.challenge.process.web.dto.OrderItemDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrderMapperTest {
    private OrderMapper orderMapper;

    @BeforeEach
    public void setUp() {
        orderMapper = new OrderMapper(); // Inicializamos el mapper antes de cada test
    }

    @Test
    public void testParseDtoToModel() {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setItemId("1");
        orderItemDTO.setQuantity(3);
        orderItemDTO.setPrice(150.0);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId("123");
        orderDTO.setCustomerId("456");
        orderDTO.setOrderStatus("PENDING");
        orderDTO.setOrderItems(List.of(orderItemDTO));

        Order order = orderMapper.parseDtoToModel(orderDTO);

        assertNotNull(order);
        assertEquals("123", order.getOrderId());
        assertEquals("456", order.getCustomerId());
        assertEquals("PENDING", order.getOrderStatus());
        assertNotNull(order.getOrderItems());
        assertEquals(1, order.getOrderItems().size());

        OrderItem orderItem = order.getOrderItems().get(0);
        assertEquals("1", orderItem.getItemId());
        assertEquals(3, orderItem.getQuantity());
        assertEquals(150.0, orderItem.getPrice());
    }
}
