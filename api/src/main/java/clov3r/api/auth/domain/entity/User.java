package clov3r.api.auth.domain.entity;

import clov3r.api.auth.domain.data.UserStatus;
import clov3r.api.auth.domain.dto.KakaoProfileDTO;
import clov3r.api.common.domain.data.Gender;
import clov3r.api.common.domain.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "user_seq_generator", sequenceName = "user_seq", allocationSize = 1)
    private Long idx;
    private String name;
    private String nickname;
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "profile_img")
    private String profileImg;

    @Enumerated(EnumType.STRING)
    private Gender gender;  //  product gender enum 과 다름

    private String age;
    private LocalDate birthDate;

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

}
