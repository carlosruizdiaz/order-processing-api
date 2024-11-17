package com.challenge.process.web.controller;

import com.challenge.process.applicaton.service.OrderService;
import com.challenge.process.infrastructure.config.RateLimiterService;
import com.challenge.process.web.dto.OrderDTO;
import com.challenge.process.web.dto.OrderItemDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest

@AutoConfigureMockMvc
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderDTO validOrderDto;
    private OrderItemDTO validOrderItemDto;

    @BeforeEach
    public void setUp() {
        validOrderDto = new OrderDTO();
        validOrderDto.setOrderId("123");
        validOrderDto.setCustomerId("456");
        validOrderDto.setOrderAmount(200.0);
        validOrderDto.setOrderStatus("PENDING");

        validOrderItemDto = new OrderItemDTO();
        validOrderItemDto.setItemId("1");
        validOrderItemDto.setQuantity(3);
        validOrderItemDto.setPrice(15.0);

        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
        orderItemDTOList.add(validOrderItemDto);
        validOrderDto.setOrderItems(orderItemDTOList);
    }

    @Test
    public void testProcessOrder_Success() throws Exception {
        mockMvc.perform(post("/api/orders/processOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validOrderDto))
                        .header("User-Id", "user1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Order is being processed."));
    }

    @Test
    public void testProcessOrder_BadRequest_InvalidOrder() throws Exception {
        OrderDTO invalidOrderDto = new OrderDTO();
        invalidOrderDto.setOrderId("");  // Id de pedido vacío

        mockMvc.perform(post("/api/orders/processOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidOrderDto))
                        .header("User-Id", "user1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Order ID cannot be null or empty."));
    }

    @Test
    public void testProcessOrder_MissingUserIdHeader() throws Exception {
        mockMvc.perform(post("/api/orders/processOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validOrderDto)))
                .andExpect(status().isBadRequest());
    }
}
