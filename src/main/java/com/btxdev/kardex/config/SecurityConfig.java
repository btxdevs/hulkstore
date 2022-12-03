package com.btxdev.kardex.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(){
        return authentication -> authentication;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/cart/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/producto").permitAll()
                .antMatchers("/api/producto/**").authenticated()
                .antMatchers("/api/usuario/**").authenticated()
                .antMatchers("/api/kardex/**").authenticated()
                .antMatchers("/usuario").authenticated()
                .antMatchers("/producto").authenticated()
                .antMatchers("/kardex").authenticated()
                .antMatchers("/shop").permitAll()
                .anyRequest().permitAll();
        return http.build();
    }
}
