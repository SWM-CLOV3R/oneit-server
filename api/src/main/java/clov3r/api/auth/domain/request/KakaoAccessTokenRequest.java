package clov3r.api.auth.domain.request;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class KakaoAccessTokenRequest {
    private String accessToken;
}
