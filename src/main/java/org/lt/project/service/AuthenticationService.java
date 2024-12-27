package org.lt.project.service;

import org.lt.project.dto.UserCreateRequestDto;
import org.lt.project.dto.UserLoginRequestDto;
import org.lt.project.dto.UserLoginResponseDto;
import org.lt.project.exception.customExceptions.UnauthorizedException;
import org.lt.project.model.User;
import org.lt.project.security.service.JwtTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserService userService, JwtTokenService jwtTokenService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtTokenService = jwtTokenService;
        this.authenticationManager = authenticationManager;
    }

    public User createUser(UserCreateRequestDto userCreateRequestDto) {
        return userService.createUser(userCreateRequestDto);
    }

    public UserLoginResponseDto authenticateUser(UserLoginRequestDto userRequestDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequestDto.username(), userRequestDto.password()));
        if (authentication.isAuthenticated()) {
            return UserLoginResponseDto.builder()
                    .username(userRequestDto.username())
                    .token(jwtTokenService.generateToken(userRequestDto.username())).build()
                    ;
        }
        throw new UnauthorizedException("Username or password is incorrect");
    }
}
