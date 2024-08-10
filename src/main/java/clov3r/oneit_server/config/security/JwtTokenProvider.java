package clov3r.oneit_server.config.security;
import clov3r.oneit_server.exception.AuthException;
import clov3r.oneit_server.response.BaseResponseStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

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
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpireTime))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(Long userId, long refreshTokenExpireTime) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("user_id", userId)
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpireTime))
                .signWith(key)
                .compact();
    }

    // validateToken 메서드는 토큰이 유효한지 확인하는 메서드입니다.
    public boolean validateToken(String token) throws RuntimeException {

        // 토큰이 만료되었는지 확인하고 만료되었다면 Exception을 발생시킵니다.
        try {
            Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
            if (claims.getExpiration().before(new Date())) {
                throw new AuthException(BaseResponseStatus.TOKEN_EXPIRED);
            }
        } catch (Exception e) {
            throw new AuthException(BaseResponseStatus.INVALID_TOKEN);
        }


        return true;

    }

    // getUserIdFromToken 메서드는 토큰에서 userId를 추출하는 메서드입니다.
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }
}
