package clov3r.api.auth.domain.dto;

import clov3r.domain.domains.entity.User;
import clov3r.domain.domains.type.Gender;
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
    private String phoneNumber;
    private Boolean isAgreeMarketing;

    public UserDTO(User user) {
        this.idx = user.getIdx();
        this.email = user.getEmail();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.profileImg = user.getProfileImg();
        this.gender = user.getGender();
        this.birthDate = user.getBirthDate();
        this.phoneNumber = user.getPhoneNumber();
        this.isAgreeMarketing = user.getIsAgreeMarketing();
    }
}
