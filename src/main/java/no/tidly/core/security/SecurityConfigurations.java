package no.tidly.core.security;

import static org.springframework.http.HttpMethod.POST;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfigurations {

        public SecurityConfigurations() {
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

                return httpSecurity
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**",
                                                                "/swagger-ui.html")
                                                .permitAll()
                                                .requestMatchers(POST, "/api/v1/companies").permitAll()
                                                .requestMatchers("/api/v1/companies/**").authenticated()
                                                .requestMatchers("/api/v1/departments/**").authenticated()
                                                .requestMatchers("/api/v1/teams/**").authenticated()
                                                .requestMatchers("/api/v1/employees/**").authenticated()
                                                .requestMatchers("/api/v1/absence-types/**").authenticated()
                                                .requestMatchers("/api/v1/holidays/**").authenticated()
                                                .requestMatchers("/api/v1/company-absence-settings/**").authenticated()
                                                .anyRequest().permitAll())
                                .oauth2ResourceServer(
                                                oauth2 -> oauth2.jwt(org.springframework.security.config.Customizer
                                                                .withDefaults()))
                                .build();
        }

}
