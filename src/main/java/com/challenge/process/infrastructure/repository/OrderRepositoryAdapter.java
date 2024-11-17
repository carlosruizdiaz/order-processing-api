package com.challenge.process.infrastructure.repository;

import com.challenge.process.domain.model.Order;
import com.challenge.process.domain.port.OrderRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    // Simulación de una base de datos en memoria o acceso a la base real
    @Override
    public void save(Order order) {
        // Guardar en la base de datos
        System.out.println("Saving order: " + order.getOrderId());
    }

}
