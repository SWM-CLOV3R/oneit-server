package clov3r.api.common.domain.DTO;

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
