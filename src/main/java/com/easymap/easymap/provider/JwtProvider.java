package com.easymap.easymap.provider;

import com.easymap.easymap.entity.User;
import com.easymap.easymap.handler.exception.AuthenticationException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey secretKey;

    public JwtProvider(@Value("${jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getEmail(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
        } catch (ExpiredJwtException e) {
            throw new AuthenticationException("JWT expired");
        }
    }

    public String getNickname(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("nickname", String.class);
        } catch (ExpiredJwtException e) {
            throw new AuthenticationException("JWT expired");
        }
    }

    public String getUserRole(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("user_role", String.class);
        } catch (ExpiredJwtException e) {
            throw new AuthenticationException("JWT expired");
        }
    }

    public String getUserOauthType(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("oauthType", String.class);
        } catch (ExpiredJwtException e) {
            throw new AuthenticationException("JWT expired");
        }
    }

    public Boolean isExpired(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            throw new AuthenticationException("JWT expired");
        }
    }

    public String generateToken(User user, Long expiredMs) {
        return Jwts.builder()
                .issuer("Easymap")
                .subject("JWT Token")
                .claim("email", user.getEmail())
                .claim("nickname", user.getNickname())
                .claim("user_role", user.getUserRole())
                .claim("oauthType", user.getOauthType())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

}
