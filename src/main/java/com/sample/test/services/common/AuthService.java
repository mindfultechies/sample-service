package com.sample.test.services.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.metrics.export.prometheus.PrometheusScrapeEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class AuthService {


    @Value("${prometheus.metrics.username}")
    private String metricsAdminUsername;

    @Value("${prometheus.metrics.password}")
    private String metricsAdminPassword;

    @Value("${prometheus.metrics.roles}")
    private String metricsAdminRole;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(httpRequests -> httpRequests
//                        .requestMatchers("/actuator/**")
//                        .hasRole("ACTUATOR_ADMIN")
//                        .requestMatchers(EndpointRequest.to(PrometheusScrapeEndpoint.class))
//                        .hasRole("METRICS_ADMIN")
                        .requestMatchers(EndpointRequest.toAnyEndpoint())
                        .permitAll()
                        .anyRequest().authenticated())
                    .httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(Customizer.withDefaults()))

                .build();
    }

    @Bean
    public UserDetailsService userDetailsManager() {

        UserDetails userDetails = User.builder()
                .username(metricsAdminUsername)
                .password("{bcrypt}"+metricsAdminPassword)
                .roles(metricsAdminRole)
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        var encoders = new HashMap<String, PasswordEncoder>(
                Map.of("bcrypt",new BCryptPasswordEncoder(),
                        "noop", NoOpPasswordEncoder.getInstance())
        );

        var e = new DelegatingPasswordEncoder("bcrypt", encoders);
        return e;
    }



}
