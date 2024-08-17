package org.lt.project.api;

import org.lt.project.dto.UserRequestDto;
import org.lt.project.dto.resultDto.Result;
import org.lt.project.dto.resultDto.SuccessResult;
import org.lt.project.service.AuthenticationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lt-api/1.0/authentication/")
public class AuthenticationApi {
    private final AuthenticationService authenticationService;

    public AuthenticationApi(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("login")
    public Result login(@RequestBody UserRequestDto requestDto) {
        return authenticationService.authenticateUser(requestDto);
    }
    @PostMapping("create")
    public Result createUser(@RequestBody UserRequestDto requestDto) {
        return authenticationService.createUser(requestDto);
    }

    @GetMapping("test")
    public Result test() {
        return new SuccessResult("selam");
    }

    @GetMapping("testAdmin")
    public Result testAdmin() {
        return new SuccessResult("selam admin");
    }

}
