package org.lt.project.service;

import org.lt.project.dto.UserRequestDto;
import org.lt.project.dto.UserResponseDto;
import org.lt.project.dto.resultDto.DataResult;
import org.lt.project.dto.resultDto.SuccessDataResult;
import org.lt.project.model.User;
import org.lt.project.security.service.JwtTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public DataResult<User> createUser(UserRequestDto userRequestDto) {
        return new SuccessDataResult<>(userService.createUser(userRequestDto));
    }

    public DataResult<UserResponseDto> authenticateUser(UserRequestDto userRequestDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequestDto.username(), userRequestDto.password()));
        if (authentication.isAuthenticated()) {
            return new SuccessDataResult<>(
                    UserResponseDto.builder()
                            .username(userRequestDto.username())
                            .token(jwtTokenService.generateToken(userRequestDto.username())).build()
            );
        }
        throw new UsernameNotFoundException("Username or password is incorrect");
    }
}
