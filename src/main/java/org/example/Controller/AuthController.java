package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Repository.UserRepository;
import org.example.Service.Security.JwtUtil;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public Map<String, String> createToken(@RequestParam String username, @RequestParam String password){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        String token = jwtUtil.generateToken(username);
        return Map.of("token", token);
    }

    @Profile("dev")
    @GetMapping("auth/test-bcrypt")
    public String testBcrypt(@RequestParam String password, @RequestParam String hash) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matches = encoder.matches(password, hash);
        return "Пароль: " + password + "\nХеш: " + hash + "\nСовпадает: " + matches;
    }

    @Profile("dev")
    @GetMapping("/auth/generate-hash")
    public String generateHash(@RequestParam String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
