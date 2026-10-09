package com.anurag.gymMemberShip.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

      @Value("${jwt.secret}")
      private String secret;

      @Value("${jwt.expiration}")
      private long expiration;

      private SecretKey getSigningKey() {
            return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
      }

      public String generateToken(String email, String role) {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + expiration);

            return Jwts.builder()
                        .subject(email)
                        .claim("role", role)
                        .issuedAt(now)
                        .expiration(expiryDate)
                        .signWith(getSigningKey())
                        .compact();
      }

      public Claims extractAllClaims(String token) {
            return Jwts.parser()
                        .verifyWith(getSigningKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
      }

      public String extractEmail(String token) {
            return extractAllClaims(token).getSubject();
      }

      public String extractRole(String token) {
            return extractAllClaims(token).get("role", String.class);
      }

      public boolean isValid(String token) {
            try {
                  Claims claims = extractAllClaims(token);
                  return !claims.getExpiration().before(new Date());
            } catch (Exception e) {
                  return false;
            }
      }
}
