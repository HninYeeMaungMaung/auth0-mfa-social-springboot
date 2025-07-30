package com.example.auth0;

import com.example.auth0.config.CustomAuthorizationRequestResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           ClientRegistrationRepository clientRegistrationRepository) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(auth -> auth
                                .authorizationRequestResolver(
                                        new CustomAuthorizationRequestResolver(
                                                clientRegistrationRepository,
                                                "/oauth2/authorization"
                                        )
                                )
                        )
                        .defaultSuccessUrl("/forward", true)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/") // You can override this to redirect to Auth0 logout
                );

        return http.build();
    }
}
