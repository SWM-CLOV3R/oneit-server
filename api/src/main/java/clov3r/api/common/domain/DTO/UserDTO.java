package clov3r.api.common.domain.DTO;

import clov3r.api.common.domain.data.Gender;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long idx;
    private String email;
    private String name;
    private String nickname;
    private String profileImg;
    private Gender gender;
    private LocalDate birthDate;
}
