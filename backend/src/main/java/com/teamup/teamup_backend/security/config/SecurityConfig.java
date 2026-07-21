package com.teamup.teamup_backend.security.config;

import com.teamup.teamup_backend.constant.ApiPaths;
import com.teamup.teamup_backend.security.filter.JwtAuthenticationFilter;
import com.teamup.teamup_backend.security.handler.JwtAuthenticationEntryPoint;
import com.teamup.teamup_backend.security.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomUserDetailsService userDetailsService,
            JwtAuthenticationEntryPoint authenticationEntryPoint
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)

                .cors(Customizer.withDefaults())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(authenticationEntryPoint))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers(ApiPaths.AUTH + "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, ApiPaths.SKILLS).permitAll()
                        .requestMatchers(HttpMethod.GET, ApiPaths.USERS + ApiPaths.USER_ID).permitAll()
                        .requestMatchers(HttpMethod.GET, ApiPaths.USERS + ApiPaths.USER_ID + ApiPaths.USER_SKILLS).permitAll()
                        .requestMatchers(HttpMethod.GET, ApiPaths.USERS + ApiPaths.SEARCH).permitAll()
                        .requestMatchers(HttpMethod.GET, ApiPaths.EVENTS, ApiPaths.EVENTS + "/**").permitAll()
                        .requestMatchers(ApiPaths.USERS + ApiPaths.CURRENT_USER,ApiPaths.USERS + ApiPaths.CURRENT_USER + "/**").authenticated()
                        .requestMatchers(HttpMethod.POST, ApiPaths.EVENTS).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, ApiPaths.EVENTS + "/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, ApiPaths.EVENTS + "/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, ApiPaths.TEAMS, ApiPaths.TEAMS + "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, ApiPaths.TEAMS).authenticated()
                        .requestMatchers(HttpMethod.PUT, ApiPaths.TEAMS + "/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, ApiPaths.TEAMS + "/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, ApiPaths.TEAMS + "/**").authenticated()
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:5173") );

        configuration.setAllowedMethods(List.of("*"));

        configuration.setAllowedHeaders(List.of("*"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}