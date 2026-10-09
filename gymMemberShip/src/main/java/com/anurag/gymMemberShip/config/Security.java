package com.anurag.gymMemberShip.config;

import com.anurag.gymMemberShip.filter.JwtAuthenticationFilter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class Security {

      @Autowired
      private JwtAuthenticationFilter jwtAuthenticationFilter;

      @Bean
      public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
      }

      @Bean
      public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
            return configuration.getAuthenticationManager();
      }

      @Bean
      public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(List.of("http://localhost:5173"));
            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(List.of("*"));
            configuration.setAllowCredentials(true);
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
      }

      @Bean
      public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                  .csrf(AbstractHttpConfigurer::disable)
                  .cors(Customizer.withDefaults())
                  .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                  .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**", "/*").permitAll()
                        .requestMatchers("/api/auth/**", "/api/auth/*").permitAll()

                        // Plans
                        .requestMatchers(HttpMethod.GET, "/api/plans/**", "/api/plans").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/plans/**", "/api/plans").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/plans/**", "/api/plans").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/plans/**", "/api/plans").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/plans/**", "/api/plans").hasRole("ADMIN")

                        // Branches
                        .requestMatchers(HttpMethod.GET, "/api/branches/**", "/api/branches").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/branches/**", "/api/branches").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/branches/**", "/api/branches").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/branches/**", "/api/branches").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/branches/**", "/api/branches").hasRole("ADMIN")

                        // Coupons
                        .requestMatchers("/api/coupons/validate").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/coupons/**", "/api/coupons").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/coupons/**", "/api/coupons").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/coupons/**", "/api/coupons").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/coupons/**", "/api/coupons").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/coupons/**", "/api/coupons").hasRole("ADMIN")

                        // Payments
                        .requestMatchers("/api/payments/my-payments").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/payments/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/payments").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/payments/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/payments/**").hasRole("ADMIN")

                        // Invoices
                        .requestMatchers("/api/invoices/my-invoices").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/invoices/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/invoices").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/invoices/**").hasRole("ADMIN")

                        // Support Tickets
                        .requestMatchers(HttpMethod.POST, "/api/tickets").authenticated()
                        .requestMatchers("/api/tickets/my-tickets").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/tickets/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/tickets").hasAnyRole("ADMIN", "SUPPORT")
                        .requestMatchers(HttpMethod.PATCH, "/api/tickets/*/status").hasAnyRole("ADMIN", "SUPPORT")
                        .requestMatchers(HttpMethod.POST, "/api/tickets/*/respond").hasAnyRole("ADMIN", "SUPPORT")
                        .requestMatchers(HttpMethod.DELETE, "/api/tickets/**").hasRole("ADMIN")

                        // Memberships
                        .requestMatchers("/api/memberships/my-membership").authenticated()
                        .requestMatchers("/api/memberships/purchase").authenticated()
                        .requestMatchers("/api/memberships/*/cancel", "/api/memberships/*/freeze", "/api/memberships/*/renew", "/api/memberships/*/upgrade").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/memberships").hasAnyRole("ADMIN", "SUPPORT")

                        // Admin & Staff Management
                        .requestMatchers("/api/employees/**", "/api/employees").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/members/**", "/api/members").hasRole("ADMIN")
                        .requestMatchers("/api/members/**", "/api/members").hasAnyRole("ADMIN", "SUPPORT")
                        .requestMatchers("/api/dashboard/**", "/api/dashboard").hasAnyRole("ADMIN", "SUPPORT")
                        .requestMatchers("/api/user/**", "/api/user").authenticated()

                        .anyRequest().authenticated()
                  )
                  .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
      }
}
