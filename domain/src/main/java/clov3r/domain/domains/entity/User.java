package clov3r.domain.domains.entity;

import clov3r.domain.domains.status.UserStatus;
import clov3r.domain.domains.type.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
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

    public void changeInactiveUser() {
        this.status = UserStatus.INACTIVE;
        deleteBaseEntity();
    }

    public void backToActiveUser() {
        this.status = UserStatus.ACTIVE;
        updateBaseEntity();
        restoreBaseEntity();
    }
}
