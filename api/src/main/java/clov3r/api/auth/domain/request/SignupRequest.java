package clov3r.api.auth.domain.request;

import clov3r.api.common.domain.data.Gender;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class SignupRequest {
    private String name;
    private String nickname;
    private LocalDate birthDate;
    private Gender gender;
}
