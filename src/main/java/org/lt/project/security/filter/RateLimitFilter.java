package org.lt.project.security.filter;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {
    @Value("${rate.limit.max-attempts:5}")
    private int MAX_FAILED_ATTEMPTS;
    
    @Value("${rate.limit.duration:300000}")
    private long BLACKLIST_DURATION;

    private final Map<String, Integer> failedAttempts = new ConcurrentHashMap<>();
    private final Map<String, Long> blacklistTimestamps = new ConcurrentHashMap<>();
    
    @Scheduled(fixedRate = 600000) // 10 dakikada bir
    public void cleanupExpiredEntries() {
        long now = System.currentTimeMillis();
        blacklistTimestamps.entrySet().removeIf(entry -> 
            (now - entry.getValue()) >= BLACKLIST_DURATION);
        log.debug("Cleanup task executed. Removed expired blacklist entries.");
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        String clientId = getClientId(request);

        if (isBlacklisted(clientId)) {
            long remainingTime = getRemainingBlacklistTime(clientId);
            log.warn("Request blocked - IP is blacklisted: {} for {} more seconds", 
                    clientId, remainingTime / 1000);
            
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(String.format(
                "{\"message\":\"IP adresiniz geçici olarak engellenmiştir. %d saniye sonra tekrar deneyiniz.\",\"statusCode\":429}",
                remainingTime / 1000
            ));
            return;
        }

        filterChain.doFilter(request, response);
    }

    public void recordFailedAttempt(String clientId) {
        int currentAttempts = failedAttempts.getOrDefault(clientId, 0) + 1;
        log.warn("Failed login attempt {} of {} for IP: {}", 
                currentAttempts, MAX_FAILED_ATTEMPTS, clientId);
        
        if (currentAttempts >= MAX_FAILED_ATTEMPTS) {
            log.warn("Max failed attempts reached. Blacklisting IP: {}", clientId);
            blacklistTimestamps.put(clientId, System.currentTimeMillis());
            failedAttempts.remove(clientId);
        } else {
            failedAttempts.put(clientId, currentAttempts);
        }
    }

    public boolean isBlacklisted(String clientId) {
        Long blacklistedAt = blacklistTimestamps.get(clientId);
        if (blacklistedAt != null) {
            long elapsedTime = System.currentTimeMillis() - blacklistedAt;
            if (elapsedTime >= BLACKLIST_DURATION) {
                log.info("Blacklist duration expired for IP: {}", clientId);
                blacklistTimestamps.remove(clientId);
                failedAttempts.remove(clientId);
                return false;
            }
            return true;
        }
        return false;
    }

    private long getRemainingBlacklistTime(String clientId) {
        Long blacklistedAt = blacklistTimestamps.get(clientId);
        if (blacklistedAt != null) {
            long elapsedTime = System.currentTimeMillis() - blacklistedAt;
            return Math.max(0, BLACKLIST_DURATION - elapsedTime);
        }
        return 0;
    }

    private String getClientId(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = request.getRemoteAddr();
        }
        // X-Forwarded-For başlığı virgülle ayrılmış IP'ler içerebilir
        if (clientIp.contains(",")) {
            clientIp = clientIp.split(",")[0].trim();
        }
        return clientIp;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.contains("/swagger-ui") || path.contains("/v3/api-docs");
    }
} 