package clov3r.api.auth.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaoLoginDTO {

    private Boolean isSignedUp;
    private String accessToken;
    private String refreshToken;

}
