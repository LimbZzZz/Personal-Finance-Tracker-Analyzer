package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Repository.UserRepository;
import org.example.Service.Security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public String createToken(@RequestParam String username, @RequestParam String password){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtUtil.generateToken(username);
    }

    @GetMapping("auth/test-bcrypt")
    public String testBcrypt(@RequestParam String password, @RequestParam String hash) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matches = encoder.matches(password, hash);
        return "Пароль: " + password + "\nХеш: " + hash + "\nСовпадает: " + matches;
    }

    @GetMapping("/auth/generate-hash")
    public String generateHash(@RequestParam String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
