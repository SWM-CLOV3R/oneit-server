package clov3r.oneit_server.domain.request;

import clov3r.oneit_server.domain.data.Gender;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class SignupRequest {
    private String name;
    private String nickname;
    private LocalDate birthDate;
    private Gender gender;
}
