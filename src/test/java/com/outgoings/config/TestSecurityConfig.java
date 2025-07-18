package com.outgoings.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@TestConfiguration
public class TestSecurityConfig {

  @Bean
  public AuthenticationProvider authenticationProvider() {
    return new AuthenticationProvider() {
      @Override
      public Authentication authenticate(Authentication authentication)
          throws AuthenticationException {
        return authentication; // accept any authentication
      }

      @Override
      public boolean supports(Class<?> authentication) {
        return true; // supports any type
      }
    };
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    return authentication -> authentication; // dummy auth
  }
}
