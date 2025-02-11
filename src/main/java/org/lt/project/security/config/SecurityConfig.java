package org.lt.project.security.config;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lt.project.security.filter.RateLimitFilter;
import org.lt.project.security.filter.TokenAuthenticationFilter;
import org.lt.project.security.filter.XssFilter;
import org.lt.project.service.UserService;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

    @Value("${allowed.origins}")
    private String allowedOrigins;

    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final XssFilter xssFilter;
  private final RateLimitFilter rateLimitFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        HttpSecurity security = http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

    configureAuthorizationRules(security, activeProfile.equals("dev"));
    return security
        .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider())
        .headers(
            headers ->
                headers
                    .xssProtection(HeadersConfigurer.XXssConfig::disable)
                    .contentSecurityPolicy(
                        csp ->
                            csp.policyDirectives(
                                "default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; "
                                    + "img-src 'self' data:; font-src 'self' data:;"))
                    .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                    .contentTypeOptions(HeadersConfigurer.ContentTypeOptionsConfig::disable))
        .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(rateLimitFilter, TokenAuthenticationFilter.class)
        .addFilterBefore(xssFilter, RateLimitFilter.class)
        .build();
  }

  private void configureAuthorizationRules(HttpSecurity security, boolean isDevProfile)
      throws Exception {
    if (isDevProfile) {
      security.authorizeHttpRequests(
          x ->
              x.requestMatchers("/authentication/register")
                  .permitAll()
                  .requestMatchers("/authentication/login")
                  .permitAll()
                  .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                  .permitAll()
                  .requestMatchers(
                      "/server/**",
                      "/abuse-key/**",
                      "/abuse/**",
                      "/log-listener/**",
                      "/log-pattern/**",
                      "/suspect-ip/**",
                      "/banned-ip/**",
                      "/settings/**",
                      "/file/**",
                      "/geo-ip-countries/**")
                  .hasRole("USER")
                  .requestMatchers("/ip-check/**")
                  .permitAll()
                  .anyRequest()
                  .authenticated());
    } else {
      security.authorizeHttpRequests(
          x ->
              x.requestMatchers("/authentication/register")
                  .denyAll()
                  .requestMatchers("/authentication/login")
                  .permitAll()
                  .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                  .denyAll()
                  .requestMatchers(
                      "/server/**",
                      "/abuse-key/**",
                      "/abuse/**",
                      "/log-listener/**",
                      "/log-pattern/**",
                      "/suspect-ip/**",
                      "/banned-ip/**",
                      "/settings/**",
                      "/file/**",
                      "/geo-ip-countries/**")
                  .hasRole("USER")
                  .requestMatchers("/ip-check/**")
                  .permitAll()
                  .anyRequest()
                  .authenticated());
    }
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