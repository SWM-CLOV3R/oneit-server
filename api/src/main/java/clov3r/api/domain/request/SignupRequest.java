package clov3r.api.domain.request;

import clov3r.api.domain.data.Gender;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class SignupRequest {
    private String name;
    private String nickname;
    private LocalDate birthDate;
    private Gender gender;
}
