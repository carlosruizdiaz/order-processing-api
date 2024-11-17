package com.challenge.process.web.controller;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MetricsController {

    private final MeterRegistry meterRegistry;

    public MetricsController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @GetMapping("/custom-metrics")
    public String customMetrics() {
        // Ejemplo de acceso directo a las métricas
        double currentCpuUsage = meterRegistry.get("system.cpu.usage").gauge().value();
        double memoryUsed = meterRegistry.get("jvm.memory.used").gauge().value();
        return String.format("CPU Usage: %.2f, Memory Used: %.2f", currentCpuUsage, memoryUsed);
    }
}
