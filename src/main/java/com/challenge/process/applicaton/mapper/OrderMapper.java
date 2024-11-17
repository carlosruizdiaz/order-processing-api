package com.challenge.process.applicaton.mapper;

import com.challenge.process.domain.model.Order;
import com.challenge.process.domain.model.OrderItem;
import com.challenge.process.web.dto.OrderDTO;
import com.challenge.process.web.dto.OrderItemDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {

    public Order parseDtoToModel(OrderDTO dto){
        Order model = new Order();

        model.setOrderId(dto.getOrderId());
        model.setCustomerId(dto.getCustomerId());
        model.setOrderAmount(model.getOrderAmount());
        model.setOrderStatus(dto.getOrderStatus());

        OrderItem orderItem;
        List<OrderItem> orderItemList = new ArrayList<OrderItem>();
        for (OrderItemDTO oi : dto.getOrderItems()) {
            orderItem = new OrderItem();
            orderItem.setItemId(oi.getItemId());
            orderItem.setQuantity(oi.getQuantity());
            orderItem.setPrice(oi.getPrice());
            orderItemList.add(orderItem);
        }
        model.setOrderItems(orderItemList);

        return model;
    }
}
