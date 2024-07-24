package clov3r.oneit_server.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {
    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret_key}") String secretKey) {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            this.key = Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid secret key");
        }
    }

    public String createAccessToken(Long userId, long accessTokenExpireTime) {

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("user_id", userId)
                .claim("uuid", createUUID())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpireTime))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(Long userId, long refreshTokenExpireTime) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("uuid", createUUID())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpireTime))
                .signWith(key)
                .compact();
    }

    public UUID createUUID() {
        return UUID.randomUUID();
    }
}
