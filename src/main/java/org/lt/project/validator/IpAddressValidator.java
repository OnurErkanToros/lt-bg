package org.lt.project.validator;

import org.springframework.stereotype.Component;

@Component
public class IpAddressValidator {
    private static final String IPV4_PATTERN = 
        "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    private static final String IPV6_PATTERN = 
        "^([0-9a-fA-F]{1,4}:){7}([0-9a-fA-F]{1,4})$";

    public boolean isValidIpAddress(String ipAddress) {
        return ipAddress != null && (
            ipAddress.matches(IPV4_PATTERN) || 
            ipAddress.matches(IPV6_PATTERN)
        );
    }
} 