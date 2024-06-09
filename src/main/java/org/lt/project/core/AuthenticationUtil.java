package org.lt.project.core;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUtil {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String getHashedPassword(String password){
        return passwordEncoder.encode(password);
    }
    public boolean isPasswordMatch(String password,String hashedPassword){
        return passwordEncoder.matches(password,hashedPassword);
    }
}
