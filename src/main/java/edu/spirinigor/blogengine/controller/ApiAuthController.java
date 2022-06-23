package edu.spirinigor.blogengine.controller;

import edu.spirinigor.blogengine.api.response.NoAuthCheckResponse;
import edu.spirinigor.blogengine.service.AuthService;
import org.aspectj.weaver.ResolvedPointcutDefinition;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public void getCaptcha(){
        authService.getCaptcha();
    }
}
