package clov3r.api.auth.domain.request;

import clov3r.domain.domains.entity.User;
import clov3r.domain.domains.type.Gender;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class SignupRequest {
    private String name;
    private String nickname;
    private LocalDate birthDate;
    private Gender gender;
    private Boolean isAgreeMarketing;

    public User toDomain() {
        return User.builder()
            .name(name)
            .nickname(nickname)
            .gender(gender)
            .birthDate(birthDate)
            .isAgreeMarketing(isAgreeMarketing)
            .build();
    }
}
