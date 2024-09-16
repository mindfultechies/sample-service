package com.sample.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import com.sample.test.services.circuitbreaker.ExternalService;

@RestController
@RequestMapping("rps/v1")
public class HealthCheckController{

  @Autowired
  ExternalService externalService;
  

   @GetMapping(value="/testCircuitBreaker", produces = {"application/json"})
    public ResponseEntity<String> testCircuitBreaker() {
          for (int i = 0; i < 50; i++) {

                externalService.callExternalService();

        }
    }

}
