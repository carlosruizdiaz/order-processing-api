package com.challenge.process;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@SpringBootApplication
@EnableAsync
@Configuration
public class OrderApiApplication {

	@Bean(name = "taskExecutor")
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10); // Número de hilos base
		executor.setMaxPoolSize(50);  // Número máximo de hilos
		executor.setQueueCapacity(100); // Capacidad máxima de la cola
		executor.setThreadNamePrefix("taskExecutor-"); // Prefijo para los nombres de los hilos
		executor.setKeepAliveSeconds(60); // Tiempo de vida de los hilos inactivos
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // Política de rechazo
		executor.initialize();

		return executor;
	}

	public static void main(String[] args) {
		SpringApplication.run(OrderApiApplication.class, args);
	}

}
