package clov3r.api.auth.domain.entity;

import clov3r.api.auth.domain.data.UserStatus;
import clov3r.api.auth.domain.dto.KakaoProfileDTO;
import clov3r.api.auth.domain.request.SignupRequest;
import clov3r.api.auth.domain.request.UpdateUserRequest;
import clov3r.api.common.domain.data.Gender;
import clov3r.api.common.domain.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String name;
    private String nickname;
    private String email;

    @Setter
    @Column(name = "phone_number")
    private String phoneNumber;

    @Setter
    @Column(name = "profile_img")
    private String profileImg;

    @Enumerated(EnumType.STRING)
    private Gender gender;  //  product gender enum 과 다름

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Setter
    @Column(name = "is_agree_marketing")
    private Boolean isAgreeMarketing;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    public User(KakaoProfileDTO kakaoProfile) {
        this.email = kakaoProfile.getKakao_account().getEmail();
        this.name = kakaoProfile.getKakao_account().getName();
        this.nickname = kakaoProfile.getProperties().getNickname();
        this.profileImg = kakaoProfile.getProperties().getProfile_image();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        this.birthDate = LocalDate.parse(
            kakaoProfile.getKakao_account().birthyear +
                kakaoProfile.getKakao_account().birthday,
            formatter);
        this.phoneNumber = kakaoProfile.getKakao_account().phone_number;
        this.createBaseEntity();
        this.status = UserStatus.ACTIVE;
    }

    public void createUser(SignupRequest signupRequest) {
        this.name = signupRequest.getName();
        this.nickname = signupRequest.getNickname();
        this.gender = signupRequest.getGender();
        this.birthDate = signupRequest.getBirthDate();
        this.isAgreeMarketing = signupRequest.getIsAgreeMarketing();
    }

    public void changeInactiveUser() {
        this.status = UserStatus.INACTIVE;
        deleteBaseEntity();
    }

    public void updateUserDate(UpdateUserRequest updateUserRequest, MultipartFile profileImage) {
        this.nickname = updateUserRequest.getNickName();
        this.birthDate = updateUserRequest.getBirthDate();
    }

    public void backToActiveUser() {
        this.status = UserStatus.ACTIVE;
        updateBaseEntity();
        restoreBaseEntity();
    }
}
