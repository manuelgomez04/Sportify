package com.salesianos.dam.sportify.security;

import com.salesianos.dam.sportify.security.exceptionhandling.JwtAccessDeniedHandler;
import com.salesianos.dam.sportify.security.exceptionhandling.JwtAuthenticationEntryPoint;
import com.salesianos.dam.sportify.security.jwt.access.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class SecurityConfig {

        private final PasswordEncoder passwordEncoder;
        private final UserDetailsService userDetailsService;
        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final JwtAuthenticationEntryPoint authenticationEntryPoint;
        private final JwtAccessDeniedHandler accessDeniedHandler;

        @Bean
        AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {

                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);

                AuthenticationManager authenticationManager = authenticationManagerBuilder
                                .authenticationProvider(authenticationProvider())
                                .build();

                return authenticationManager;
        }

        @Bean
        DaoAuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider p = new DaoAuthenticationProvider();

                p.setUserDetailsService(userDetailsService);
                p.setPasswordEncoder(passwordEncoder);
                return p;

        }

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .csrf(csrf -> csrf.disable());
                http.sessionManagement((session) -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                http.exceptionHandling(excepz -> excepz
                                .authenticationEntryPoint(authenticationEntryPoint)
                                .accessDeniedHandler(accessDeniedHandler));
                http.authorizeHttpRequests(authz -> authz
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                                .requestMatchers("/swagger-ui*/**", "/api-docs*/**", "/swagger-resources/**",
                                                "/webjars/**", "/api-docs/swagger-config")
                                .permitAll()
                                .requestMatchers("/download/**").permitAll()
                                .requestMatchers("/uploads/**").permitAll()
                                .requestMatchers("/assets/**").permitAll()

                                .requestMatchers(HttpMethod.POST, "/writer/auth/register", "/user/auth/register",
                                                "/auth/login", "/auth/refresh/token", "/activate/account/", "/error")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/me/admin").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/admin/auth/register").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/me").authenticated()
                                .requestMatchers(HttpMethod.GET, "/me/writer").hasRole("WRITER")
                                .requestMatchers(HttpMethod.PUT, "/edit/me").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/edit/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/delete/me").hasAnyRole("USER", "WRITER", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/admin/delete/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/noticias").hasAnyRole("WRITER", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/noticias/edit/**").hasAnyRole("WRITER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/noticias").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/noticias/delete/**").hasAnyRole("WRITER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/deporte").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/deporte/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/liga").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/liga/delete/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/equipo").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/equipo/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/seguirEquipo").hasAnyRole("USER", "WRITER", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/unfollowEquipo")
                                .hasAnyRole("USER", "WRITER", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/seguirDeporte").hasAnyRole("USER", "WRITER", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/unfollowDeporte")
                                .hasAnyRole("USER", "WRITER", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/seguirLiga").hasAnyRole("USER", "WRITER", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/unfollowLiga").hasAnyRole("USER", "WRITER", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/noticias/addDeporte/**")
                                .hasAnyRole("WRITER", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/noticias/addEquipo/**").hasAnyRole("WRITER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/ligasFavoritas").authenticated()
                                .requestMatchers(HttpMethod.GET, "/deportesFavoritos").authenticated()
                                .requestMatchers(HttpMethod.GET, "/equiposFavoritos").authenticated()
                                .requestMatchers(HttpMethod.POST, "/like").authenticated()
                                .requestMatchers(HttpMethod.GET, "/noticias/filtradas").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/like/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/noticiasLiked").authenticated()
                                .anyRequest().authenticated());

                http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

                return http.build();
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOriginPatterns(
                                List.of("http://localhost:4200", "http://host.docker.internal:4200"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(
                                List.of("Authorization", "Content-Type", "content-type", "Accept", "X-Requested-With"));
                configuration.setAllowCredentials(true);
                configuration.setMaxAge(3600L);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

}
