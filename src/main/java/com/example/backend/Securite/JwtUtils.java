package com.example.backend.Securite;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    private String jwtSecret = "MzI1YzhjYTM0MWE0ZGFmYjU3M2U0ZTk4Zjk0ZDA0ZGRjMzA3YmU0NjU0M2I0YmZlZDYyN2U1ZGI5ZTE4M2I0YmRjYjY4ZDRmZDYzYmU0Zjk4ZDA0ZGRjMzA3YmU0NjU0M2I0YmZlZDYyN2U1ZGI5ZTE4M2I0Yg==";
    private int jwtExpirationMs = 86400000; // 24h

    public String generateJwtToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("role", userPrincipal.getAuthorities().iterator().next().getAuthority())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) { return false; }
    }
}