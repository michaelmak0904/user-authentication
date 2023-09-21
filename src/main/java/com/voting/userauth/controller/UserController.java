package com.voting.userauth.controller;

import com.voting.userauth.component.JwtTokenUtil;
import com.voting.userauth.model.User;
import com.voting.userauth.payload.MessageResponse;
import com.voting.userauth.payload.SigninRequest;
import com.voting.userauth.payload.SignupRequest;
import com.voting.userauth.payload.VerifyRequest;
import com.voting.userauth.repository.UserRepository;
import com.voting.userauth.service.JwtService;
import com.voting.userauth.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.*;

/**
 * @author michaelmak
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtService jwtService;

    @Autowired
    UserRepository userRepository;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@Valid @RequestBody VerifyRequest verifyRequest) {
        try {
            if (jwtTokenUtil.verifyToken(verifyRequest.getJwtToken())){
                return ResponseEntity.ok("Token is valid.");
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new MessageResponse("Token is invalid or expired"));
            }
        } catch (Exception e) {
            // Handle any exceptions that may occur during token verification
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Token verification failed"));
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody SigninRequest signinRequest){
        // Attempt to authenticate the user
        Optional<User> authenticatedUser = userService.login(signinRequest.getUsername(), signinRequest.getPassword());

        if (authenticatedUser.isPresent()) {
            // User is authenticated, generate a JWT token and return it
            return ResponseEntity.ok(new MessageResponse(jwtTokenUtil.generateToken(authenticatedUser.get())));
        } else {
            // Authentication failed, return an error response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Authentication failed"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest ){

        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // Create new user's account
        User user = new User(signupRequest.getUsername(),signupRequest.getEmail(),
                signupRequest.getPassword());

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
