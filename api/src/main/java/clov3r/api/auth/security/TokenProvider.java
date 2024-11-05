package clov3r.api.auth.security;

import clov3r.api.auth.domain.data.AuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24;  // 24 hour
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14;  // 14 days
    private final JwtTokenProvider jwtTokenProvider;

    public AuthToken createToken(Long userId) {
        String accessToken = jwtTokenProvider.createAccessToken(userId, ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId, REFRESH_TOKEN_EXPIRE_TIME);
        return new AuthToken(
                accessToken,
                BEARER_TYPE,
                refreshToken,
                ACCESS_TOKEN_EXPIRE_TIME,
                "login read",
                REFRESH_TOKEN_EXPIRE_TIME
        );
    }

}
