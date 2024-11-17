package com.challenge.process.domain.port;

import com.challenge.process.domain.model.Order;

public interface OrderRepositoryPort {
    void save(Order order);
}
