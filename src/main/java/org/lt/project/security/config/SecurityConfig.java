package org.lt.project.security.config;

import org.lt.project.security.filter.TokenAuthenticationFilter;
import org.lt.project.security.filter.RateLimitFilter;
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

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.lt.project.security.filter.XssFilter;
import java.util.Arrays;


@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@Slf4j
public class SecurityConfig {
    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

    @Value("${allowed.origins}")
    private String allowedOrigins;

    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final RateLimitFilter rateLimitFilter;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final XssFilter xssFilter;

    public SecurityConfig(TokenAuthenticationFilter tokenAuthenticationFilter,
            RateLimitFilter rateLimitFilter,
            UserService userService,
            XssFilter xssFilter,
            PasswordEncoder passwordEncoder) {
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
        this.rateLimitFilter = rateLimitFilter;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.xssFilter = xssFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        HttpSecurity security = http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // Swagger erişimi için profile kontrolü
        if ("dev".equals(activeProfile)) {
            security.authorizeHttpRequests(x -> x
                    .requestMatchers("/authentication/register").permitAll()
                    .requestMatchers("/authentication/login").permitAll()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .requestMatchers("/server/**").hasRole("USER")
                    .requestMatchers("/abuse-key/**").hasRole("USER")
                    .requestMatchers("/abuse/**").hasRole("USER")
                    .requestMatchers("/log-listener/**").hasRole("USER")
                    .requestMatchers("/log-pattern/**").hasRole("USER")
                    .requestMatchers("/suspect-ip/**").hasRole("USER")
                    .requestMatchers("/banned-ip/**").hasRole("USER")
                    .requestMatchers("/settings/**").hasRole("USER")
                    .requestMatchers("/file/**").hasRole("USER")
                    .anyRequest().authenticated());
        } else {
            security.authorizeHttpRequests(x -> x
                    .requestMatchers("/authentication/create").permitAll()
                    .requestMatchers("/authentication/login").permitAll()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").denyAll() // Swagger'a erişimi engelle
                    .requestMatchers("/server/**").hasRole("USER")
                    .requestMatchers("/abuse-key/**").hasRole("USER")
                    .requestMatchers("/abuse/**").hasRole("USER")
                    .requestMatchers("/log-listener/**").hasRole("USER")
                    .requestMatchers("/log-pattern/**").hasRole("USER")
                    .requestMatchers("/suspect-ip/**").hasRole("USER")
                    .requestMatchers("/banned-ip/**").hasRole("USER")
                    .requestMatchers("/settings/**").hasRole("USER")
                    .requestMatchers("/file/**").hasRole("USER")
                    .anyRequest().authenticated());
        }

        return security
                .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .headers(headers -> headers
                    .xssProtection(xss -> xss.disable())
                    .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'"))
                    .frameOptions(frame -> frame.deny())
                    .contentTypeOptions(content -> content.disable())
                )
                .addFilterBefore(xssFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(rateLimitFilter, XssFilter.class)
                .addFilterBefore(tokenAuthenticationFilter, RateLimitFilter.class)
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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Origins
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        
        // HTTP Methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        
        // Headers
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "Accept",
            "Origin",
            "X-Requested-With",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));
        
        // Exposed Headers
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization",
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials"
        ));
        
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); 
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}