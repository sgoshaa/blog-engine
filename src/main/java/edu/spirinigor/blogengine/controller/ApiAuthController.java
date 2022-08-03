package edu.spirinigor.blogengine.controller;

import edu.spirinigor.blogengine.api.request.CreateUserRequest;
import edu.spirinigor.blogengine.api.request.LoginRequest;
import edu.spirinigor.blogengine.api.request.PasswordRecoveryRequest;
import edu.spirinigor.blogengine.api.response.CaptchaResponse;
import edu.spirinigor.blogengine.api.response.LoginResponse;
import edu.spirinigor.blogengine.api.response.LogoutResponse;
import edu.spirinigor.blogengine.api.response.Response;
import edu.spirinigor.blogengine.service.AuthService;
import edu.spirinigor.blogengine.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final EmailService emailService;

    public ApiAuthController(AuthService authService, EmailService emailService) {
        this.authService = authService;
        this.emailService = emailService;
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
    public ResponseEntity<Response> createUser(@RequestBody CreateUserRequest userDto){
       return ResponseEntity.ok(authService.createUser(userDto));
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse>login(@RequestBody LoginRequest loginRequest){
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("logout")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<LogoutResponse> logout(){
        return ResponseEntity.ok(authService.logout());
    }

    @PostMapping("restore")
    public ResponseEntity<Response> passwordRecovery(@RequestBody PasswordRecoveryRequest passwordRecoveryRequest){
        return ResponseEntity.ok(emailService.sendRecoveryEmail(passwordRecoveryRequest));
    }

    @PostMapping("password")
    public ResponseEntity<Response>changePassword(@RequestBody PasswordRecoveryRequest passwordRecoveryRequest){
        return ResponseEntity.ok(authService.changePassword(passwordRecoveryRequest));
    }
}
