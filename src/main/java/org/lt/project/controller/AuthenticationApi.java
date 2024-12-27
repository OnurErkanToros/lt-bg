package org.lt.project.controller;

import org.lt.project.dto.UserCreateRequestDto;
import org.lt.project.dto.UserLoginRequestDto;
import org.lt.project.dto.UserLoginResponseDto;
import org.lt.project.model.User;
import org.lt.project.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lt-api/1.0/authentication/")
public class AuthenticationApi {
    private final AuthenticationService authenticationService;

    public AuthenticationApi(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto requestDto) {
        return ResponseEntity.ok(authenticationService.authenticateUser(requestDto));
    }

    @PostMapping("create")
    public ResponseEntity<User> createUser(@RequestBody UserCreateRequestDto requestDto) {
        return ResponseEntity.ok(authenticationService.createUser(requestDto));
    }
}
