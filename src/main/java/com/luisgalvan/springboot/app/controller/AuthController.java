package com.luisgalvan.springboot.app.controller;

import com.luisgalvan.springboot.app.dto.AuthenticationResponse;
import com.luisgalvan.springboot.app.dto.LoginRequest;
import com.luisgalvan.springboot.app.dto.RegisterRequest;
import com.luisgalvan.springboot.app.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registeredRequest){
        authService.signup(registeredRequest);
        return new ResponseEntity<>("User registration success", HttpStatus.OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated successfuly",HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
     return authService.login(loginRequest);
    }

}
