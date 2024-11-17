package com.challenge.process.infrastructure.adapter;

import com.challenge.process.domain.model.Order;
import com.challenge.process.domain.port.OrderProcessingPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.*;

@Service
public class OrderProcessingAdapter implements OrderProcessingPort {

    private static final Logger logger = LoggerFactory.getLogger(OrderProcessingAdapter.class);

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    @Qualifier("taskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    private  Counter ordersProcessed;
    private  Timer orderProcessingTimer;

    @Async("taskExecutor")
    @CircuitBreaker(name = "orderProcessingCircuitBreaker", fallbackMethod = "fallbackProcessOrder")
    @Retry(name = "orderProcessingRetry")
    public void processOrder(Order order) {
        logger.info("Start processing order: {}", order.getOrderId());

        this.ordersProcessed =  meterRegistry.counter("orders.processed");
        this.orderProcessingTimer = meterRegistry.timer("order.processing.time");
        Timer.Sample sample = Timer.start(meterRegistry); // Iniciar el temporizador

        try {
            CompletableFuture<Double> orderProcessing = executeProcess(order);
            Double totalAmount = orderProcessing.get();
            order.setOrderAmount(totalAmount);

            ordersProcessed.increment();  // Contador de pedidos procesados
            logger.info("Order processing complete: {}", order.getOrderId());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            sample.stop(orderProcessingTimer);
        }
    }

    public CompletableFuture<Double> executeProcess(Order order) {
        /*
        1. Obtener los detalles del pedido asincrónicamente
        CompletableFuture.supplyAsync(): Lanza una tarea asincrónica que devuelve un valor (detalles del pedido).
         */
        CompletableFuture<String> orderDetails = CompletableFuture.supplyAsync(() -> {
            logger.debug("Fetching details for order: {}", order.getOrderId());
            return getOrderDetails(order.getOrderId());  // Simula obtener los detalles del pedido
        }, taskExecutor);

        /*
        2. Calcular el precio del pedido asincrónicamente
        thenApplyAsync(): Permite encadenar una tarea que depende de la tarea anterior (en este caso, calcular el precio del pedido).
        */
        CompletableFuture<Double> calculatedPrice = orderDetails.thenApplyAsync(o -> {
            logger.debug("Calculating price for order: {}", order.getOrderId());
            return calculateOrderPrice(o);  // Calculamos el precio basado en los detalles
        }, taskExecutor).exceptionally(ex -> {
            System.err.println("Error en el calculo de precio: " + ex.getMessage());
            return 0.0;  // Valor predeterminado en caso de error
        });

        return calculatedPrice;
    }

    public  String getOrderDetails(String orderId) {
        simulateDelay();
        System.out.println("Order details for " + orderId + " fetched.");
        return "Order details for " + orderId;  // Simula un detalle del pedido
    }

    public  double calculateOrderPrice(String orderDetails) {
        simulateDelay();
        System.out.println("Calculating price based on: " + orderDetails);
        return 456.23;
    }

    private static void simulateDelay() {
        try {
            Random random = new Random();
            int delay = 100 + random.nextInt(400);
            TimeUnit.MILLISECONDS.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void fallbackProcessOrder(Order order, Throwable t) {
        logger.error("Fallback: Order {} could not be processed due to error: {}", order.getOrderId(), t.getMessage());
        order.setOrderAmount(0.0); // Valor por defecto
        ordersProcessed.increment();
    }
}
