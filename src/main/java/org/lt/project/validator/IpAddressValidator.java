package org.lt.project.validator;

import org.springframework.stereotype.Component;

@Component
public class IpAddressValidator {
    private static final String IPV4_PATTERN = 
        "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    private static final String IPV6_PATTERN =
            "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|" +        // Normal IPv6 (8 blok)
                    "^(?:[0-9a-fA-F]{1,4}:){1,7}:|" +                     // Sondaki `::`
                    "^:(?::[0-9a-fA-F]{1,4}){1,7}|" +                     // Başta `::`
                    "^(?:[0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|" +     // `::` bir yeri dolduruyor
                    "^(?:[0-9a-fA-F]{1,4}:){1,5}(?::[0-9a-fA-F]{1,4}){1,2}|" +
                    "^(?:[0-9a-fA-F]{1,4}:){1,4}(?::[0-9a-fA-F]{1,4}){1,3}|" +
                    "^(?:[0-9a-fA-F]{1,4}:){1,3}(?::[0-9a-fA-F]{1,4}){1,4}|" +
                    "^(?:[0-9a-fA-F]{1,4}:){1,2}(?::[0-9a-fA-F]{1,4}){1,5}|" +
                    "^[0-9a-fA-F]{1,4}:(?::[0-9a-fA-F]{1,4}){1,6}|" +
                    "^:(?::[0-9a-fA-F]{1,4}){1,7}|::|" +                 // Tek `::` geçerli
                    "^(?:[0-9a-fA-F]{1,4}:){6}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." + // IPv4-Mapped IPv6
                    "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    public boolean isValidIpAddress(String ipAddress) {
        return ipAddress != null && (
            ipAddress.matches(IPV4_PATTERN) || 
            ipAddress.matches(IPV6_PATTERN)
        );
    }
} 