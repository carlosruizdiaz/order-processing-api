package com.challenge.process.domain.port;

import com.challenge.process.domain.model.Order;

public interface OrderProcessingPort {
    void processOrder( Order order);
}
