package edu.spirinigor.blogengine.controller;

import edu.spirinigor.blogengine.api.request.CreateUserRequest;
import edu.spirinigor.blogengine.api.request.LoginRequest;
import edu.spirinigor.blogengine.api.response.CaptchaResponse;
import edu.spirinigor.blogengine.api.response.CreateUserResponse;
import edu.spirinigor.blogengine.api.response.LoginResponse;
import edu.spirinigor.blogengine.api.response.NoAuthCheckResponse;
import edu.spirinigor.blogengine.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController()
@RequestMapping("/api/auth/")
public class ApiAuthController {

    private final AuthService authService;

    public ApiAuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("check")
    public ResponseEntity<LoginResponse> getAuthCheck(Principal principal){
        return new ResponseEntity<>(authService.authCheck(principal), HttpStatus.OK);
    }

    @GetMapping("captcha")
    public ResponseEntity<CaptchaResponse> getCaptcha(){
        return ResponseEntity.ok(authService.getCaptcha());
    }

    @PostMapping("register")
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest userDto){
       return ResponseEntity.ok(authService.createUser(userDto));
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse>login(@RequestBody LoginRequest loginRequest){
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }
}
