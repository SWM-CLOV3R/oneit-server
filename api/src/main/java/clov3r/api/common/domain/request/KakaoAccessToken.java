package clov3r.api.common.domain.request;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class KakaoAccessToken {
    private String accessToken;
}
