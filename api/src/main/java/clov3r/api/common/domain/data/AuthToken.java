package clov3r.api.common.domain.data;

import lombok.Data;

@Data
public class AuthToken {
    public String accessToken;
    public String tokenType;
    public String refreshToken;
    public Long expiresIn;
    public String scope;
    public Long refreshTokenExpiresIn;

    public AuthToken(String accessToken, String tokenType, String refreshToken, Long expiresIn, String scope, Long refreshTokenExpiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.scope = scope;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }
}
