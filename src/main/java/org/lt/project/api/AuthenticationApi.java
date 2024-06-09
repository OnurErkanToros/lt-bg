package org.lt.project.api;

import org.lt.project.core.result.Result;
import org.lt.project.dto.AuthenticationRequestDto;
import org.lt.project.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lt-api/1.0/authentication/")
public class AuthenticationApi {
    private final UserService userService;
    public AuthenticationApi(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("login")
    public Result login(@RequestBody AuthenticationRequestDto requestDto){
        return userService.login(requestDto);
    }
    @PostMapping("create")
    public Result createUser(@RequestBody AuthenticationRequestDto requestDto){
        return userService.getHashedPassword(requestDto.getPassword());
    }

}
