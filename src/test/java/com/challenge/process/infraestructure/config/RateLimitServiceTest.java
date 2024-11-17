package com.challenge.process.infraestructure.config;

import com.challenge.process.infrastructure.config.RateLimiterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RateLimitServiceTest {

    private RateLimiterService rateLimiterService;

    @BeforeEach
    public void setUp() {
        rateLimiterService = new RateLimiterService();
    }

    @Test
    public void testIsRequestAllowed_FirstRequest() {
        // Primera solicitud, debería ser permitida
        assertTrue(rateLimiterService.isRequestAllowed(), "La primera solicitud debería ser permitida");
    }

    @Test
    public void testIsRequestAllowed_WithinRateLimit() throws InterruptedException {
        Thread.sleep(1000);
        // Hacemos 5 solicitudes, todas deberían ser permitidas
        for (int i = 0; i < 3; i++) {
            assertTrue(rateLimiterService.isRequestAllowed(), "La solicitud " + (i + 1) + " debería ser permitida");
        }
    }

    @Test
    public void testIsRequestAllowed_ExceedRateLimit() throws InterruptedException {
        Thread.sleep(1000);
        // Realizamos 6 solicitudes, la sexta debería ser rechazada
        for (int i = 0; i < 3; i++) {
            assertTrue(rateLimiterService.isRequestAllowed(), "La solicitud " + (i + 1) + " debería ser permitida");
        }

        // Aquí la sexta solicitud debería ser rechazada
        assertFalse(rateLimiterService.isRequestAllowed(), "La sexta solicitud debería ser rechazada");
    }

    @Test
    public void testIsRequestAllowed_AfterWaiting() throws InterruptedException {
        // Realizamos 5 solicitudes
        Thread.sleep(1000);
        for (int i = 0; i < 3; i++) {
            assertTrue(rateLimiterService.isRequestAllowed(), "La solicitud " + (i + 1) + " debería ser permitida");
        }

        // Simulamos una espera de 1 segundo para permitir que se restablezca el limitador de tasas
        Thread.sleep(1000);

        // La siguiente solicitud debería ser permitida después de esperar
        assertTrue(rateLimiterService.isRequestAllowed(), "La solicitud después de esperar debería ser permitida");
    }
}

