package com.ashwin.java.springbootjwt.api;

import com.ashwin.java.springbootjwt.auth.JwtUtil;
import com.ashwin.java.springbootjwt.auth.MyUserDetailService;
import com.ashwin.java.springbootjwt.model.AuthRequest;
import com.ashwin.java.springbootjwt.model.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloResource {
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private MyUserDetailService userDetailService;

    @Autowired
    private JwtUtil jwtUtil;

    @RequestMapping("/auth")
    @PostMapping
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
            authManager.authenticate(userToken);

            final UserDetails userDetails = userDetailService.loadUserByUsername(authRequest.getUsername());
            final String jwt = jwtUtil.generateToken(userDetails);

            final AuthResponse authResponse = new AuthResponse(jwt);
            return ResponseEntity.ok(authResponse);
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
    }

    @RequestMapping("/hello")
    @GetMapping
    public String hello() {
        return "Hello world";
    }
}
