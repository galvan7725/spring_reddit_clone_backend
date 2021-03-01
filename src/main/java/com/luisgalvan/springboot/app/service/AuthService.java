package com.luisgalvan.springboot.app.service;

import com.luisgalvan.springboot.app.dto.LoginRequest;
import com.luisgalvan.springboot.app.dto.RegisterRequest;
import com.luisgalvan.springboot.app.exceptions.SpringRedditException;
import com.luisgalvan.springboot.app.model.NotificationEmail;
import com.luisgalvan.springboot.app.model.User;
import com.luisgalvan.springboot.app.model.VerificationToken;
import com.luisgalvan.springboot.app.repository.UserRepository;
import com.luisgalvan.springboot.app.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void signup(RegisterRequest registerRequest){
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode( registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

       String token = generateVerificationToken(user);
       mailService.sendMail(new NotificationEmail("Please activate your account",
               user.getEmail(),"Thank you for siging up to the application,"+
               "please click on the below url to activate your account : "+
               "http://localhost:8080/api/auth/accountVerification/"+token));
    }

    private String generateVerificationToken(User user){
        String token =  UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
      Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
      verificationToken.orElseThrow(()-> new SpringRedditException("Invalid Token"));
      fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    void fetchUserAndEnable(VerificationToken verificationToken) {
       String username =  verificationToken.getUser().getUsername();
       User user = userRepository.findByUsername(username).orElseThrow(()->new SpringRedditException("User not found with name "+ username));
       user.setEnabled(true);
       userRepository.save(user);
    }

    public void login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
    }
}