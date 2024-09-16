package com.sample.test.services.circuitbreaker;

import org.springframework.stereotype.Service;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.*;

@Service
public class ExternalService{

    @CircuitBreaker(name = "tst-service", fallbackMethod = "fallback")
    public ResponseEntity<String> callExternalService(String url)  {
        throw new RuntimeException("Simulated failure");
    }

    public ResponseEntity<String> fallback(String url, Throwable throwable) {
        logger.debug(" service is down. Circuit is open..", throwable);
        return ResponseEntity.ok("Fallback response");
    }

  
}
