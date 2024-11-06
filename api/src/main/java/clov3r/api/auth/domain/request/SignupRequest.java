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

    public User update(User user) {
        user.setName(name);
        user.setNickname(nickname);
        user.setGender(gender);
        user.setBirthDate(birthDate);
        user.setIsAgreeMarketing(isAgreeMarketing);
        return user;
    }

}
