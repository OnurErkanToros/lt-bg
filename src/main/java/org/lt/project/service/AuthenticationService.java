package org.lt.project.service;


import org.lt.project.dto.RegisterRequest;
import org.lt.project.dto.UserCreateRequestDto;
import org.lt.project.dto.UserLoginRequestDto;
import org.lt.project.dto.UserLoginResponseDto;
import org.lt.project.dto.UserRegisterResponseDto;
import org.lt.project.exception.customExceptions.BadRequestException;
import org.lt.project.exception.customExceptions.UnauthorizedException;
import org.lt.project.model.User;
import org.lt.project.repository.UserRepository;
import org.lt.project.security.service.JwtTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Set;

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
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequestDto.getUsername(), userRequestDto.getPassword()));
        if (authentication.isAuthenticated()) {
            return UserLoginResponseDto.builder()
                    .username(userRequestDto.getUsername())
                    .token(jwtTokenService.generateToken(userRequestDto.getUsername())).build()
                    ;
        }
        throw new UnauthorizedException("Username or password is incorrect");
    }

    public UserRegisterResponseDto registerUser(RegisterRequest registerRequest) {
        if (userService.existsByUsername(registerRequest.getUsername())) {
            throw new BadRequestException("Bu kullanıcı adı zaten kullanılıyor");
        }

        UserCreateRequestDto createRequest = UserCreateRequestDto.builder()
                .username(registerRequest.getUsername())
                .password(registerRequest.getPassword())
                .authorities(Set.of(User.Role.ROLE_USER))
                .build();

        User user = createUser(createRequest);

        return UserRegisterResponseDto.builder()
                .username(user.getUsername())
                .token(jwtTokenService.generateToken(user.getUsername()))
                .message("Kullanıcı başarıyla oluşturuldu")
                .build();
    }

    public boolean isValidUsername(String username) {
        if (username == null || username.length() < 4) {
            return false;
        }
        if(userService.existsByUsername(username)){
            return false;
        }
        return username.matches("[a-zA-Z0-9]+");

    }

    public boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
}
