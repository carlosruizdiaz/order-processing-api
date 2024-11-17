package com.challenge.process.web.controller;

import com.challenge.process.applicaton.service.OrderService;
import com.challenge.process.infrastructure.config.RateLimiterService;
import com.challenge.process.web.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RateLimiterService rateLimiterService;

/*    @Autowired
    private RateLimiterServiceRedis rateLimiterServiceRedis;*/

    @PostMapping("/processOrder")
    public ResponseEntity<String> processOrder(@RequestBody OrderDTO orderDto, @RequestHeader("User-Id") String userId) {
        // Verificar si la solicitud es permitida según el rate limit
        if (!rateLimiterService.isRequestAllowed()) {
            return ResponseEntity.status(429)  // HTTP 429 Too Many Requests
                    .body("Rate limit exceeded. Please try again later.");
        }

    /*    // Verificar si la solicitud es permitida según el rate limit
        if (!rateLimiterServiceRedis.isRequestAllowed(userId)) {
            return ResponseEntity.status(429)
                    .body("Rate limit exceeded. Please try again later.");
        }*/

        try {
            orderService.processOrder(orderDto);
            return ResponseEntity.ok("Order is being processed.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
