package org.lt.project.core.conf;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class SecurityConfig {
    @Bean
    public FilterRegistrationBean<TokenAuthenticationFilter> tokenAuthenticationFilterRegistration() {
        FilterRegistrationBean<TokenAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TokenAuthenticationFilter());
        registrationBean.addUrlPatterns("/lt-api/1.0/abuse/*");
        registrationBean.addUrlPatterns("/lt-api/1.0/log-listener/*");
        registrationBean.addUrlPatterns("/lt-api/1.0/log-pattern/*");
        registrationBean.addUrlPatterns("/lt-api/1.0/abuse-key/*");
        registrationBean.addUrlPatterns("/lt-api/1.0/all-suspect-ip/*");
        registrationBean.addUrlPatterns("/lt-api/1.0/server/*");
        registrationBean.addUrlPatterns("/lt-api/1.0/suspect-ip/*");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }
}
