package org.example.external;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;

@SpringBootApplication
public class ExternalApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExternalApplication.class, args);
    }

    @RestController
    public static class Controller {
        @GetMapping
        public Mono<String> get(
                @RequestParam(required = false, defaultValue = "0") Long delay
        ) {
            return Mono.fromSupplier(() -> Thread.currentThread().toString())
                       .delayElement(Duration.ofMillis(delay));
        }
    }
}
