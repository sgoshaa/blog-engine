package edu.spirinigor.blogengine.controller;

import edu.spirinigor.blogengine.api.request.CreateUserRequest;
import edu.spirinigor.blogengine.api.response.CaptchaResponse;
import edu.spirinigor.blogengine.api.response.NoAuthCheckResponse;
import edu.spirinigor.blogengine.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/auth/")
public class ApiAuthController {

    private final AuthService authService;

    public ApiAuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("check")
    public ResponseEntity<NoAuthCheckResponse> getAuthCheck(){
        return new ResponseEntity<>(authService.authCheck(), HttpStatus.OK);
    }

    @GetMapping("captcha")
    public CaptchaResponse getCaptcha(){
        return authService.getCaptcha();
    }

    @PostMapping("register")
    public void createUser(@RequestBody CreateUserRequest userDto){
        authService.createUser(userDto);
    }
}
