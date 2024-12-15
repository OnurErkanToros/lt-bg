package org.lt.project.security.config;

import org.lt.project.security.filter.TokenAuthenticationFilter;
import org.lt.project.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {
    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(TokenAuthenticationFilter tokenAuthenticationFilter, UserService userService, PasswordEncoder passwordEncoder) {
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors->cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(x ->
                        x.requestMatchers("/lt-api/1.0/authentication/create").permitAll()
                                .requestMatchers("/lt-api/1.0/authentication/login").permitAll()
                )
                .authorizeHttpRequests(x->
                        x.requestMatchers("/swagger-ui/*").permitAll()
                                .requestMatchers("/v3/*").permitAll()
                                .requestMatchers("/v3/api-docs/*").permitAll())
                .authorizeHttpRequests(x ->
                        x.requestMatchers("/lt-api/1.0/authentication/test").hasRole("USER")
                                .requestMatchers("/lt-api/1.0/authentication/testAdmin").hasRole("ADMIN")
                                .requestMatchers("/lt-api/1.0/server/*").hasRole("USER")
                                .requestMatchers("/lt-api/1.0/server/delete/*").hasRole("USER")
                                .requestMatchers("/lt-api/1.0/server/delete").hasRole("USER")
                                .requestMatchers("/lt-api/1.0/abuse-key/*").hasRole("USER")
                                .requestMatchers("/lt-api/1.0/abuse/*").hasRole("USER")
                                .requestMatchers("/lt-api/1.0/abuse/blacklist/*").hasRole("USER")
                                .requestMatchers("/lt-api/1.0/abuse/check-ip/*").hasRole("USER")
                                .requestMatchers("/lt-api/1.0/log-listener/*").hasRole("USER")
                                .requestMatchers("/lt-api/1.0/log-pattern/*").hasRole("USER")
                                .requestMatchers("/lt-api/1.0/log-pattern/delete/*").hasRole("USER")
                                .requestMatchers("/lt-api/1.0/suspect-ip/*").hasRole("USER")
                                .requestMatchers("/lt-api/1.0/banned-ip/*").hasRole("USER")
                                .requestMatchers("/lt-api/1.0/abuse-key/delete/*").hasRole("USER")
                )
                .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}