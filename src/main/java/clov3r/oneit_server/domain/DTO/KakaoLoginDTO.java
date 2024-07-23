package clov3r.oneit_server.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaoLoginDTO {

    private Long idx;
    private String email;
    private String jwt;

}
