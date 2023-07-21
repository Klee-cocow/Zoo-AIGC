package com.animal.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author 咏鹅
 * @version 1.0
 * @description TODO
 * @date 2023/7/21 23:54
 */
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        Customizer<CsrfConfigurer<HttpSecurity>> csrfCustomizer = httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable();
        Customizer<SessionManagementConfigurer<HttpSecurity>> session = sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.csrf(csrfCustomizer)
                .sessionManagement(session)
                .build();
    }

}
