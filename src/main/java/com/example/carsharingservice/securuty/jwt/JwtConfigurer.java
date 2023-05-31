//package com.example.carsharingservice.securuty.jwt;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.DefaultSecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class JwtConfigurer extends
//        SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Override
//    public void configure(HttpSecurity builder) throws Exception {
//        JwtTokenFilter jwtTokenFilter = new JwtTokenFilter(jwtTokenProvider);
//        builder.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
//    }
//}
