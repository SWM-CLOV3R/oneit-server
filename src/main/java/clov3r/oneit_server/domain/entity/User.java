package clov3r.oneit_server.domain.entity;

import clov3r.oneit_server.domain.data.Gender;
import clov3r.oneit_server.domain.data.status.UserStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "user_seq_generator", sequenceName = "user_seq", allocationSize = 1)
    private Long idx;
    private String name;
    private String nickname;
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "nickname_from_kakao")
    private String nicknameFromKakao;

    @Column(name = "profile_img")
    private String profileImg;

    @Column(name = "profile_img_from_kakao")
    private String profileImgFromKakao;

    @Enumerated(EnumType.STRING)
    private Gender gender;  //  product gender enum 과 다름

    private String age;
    private LocalDate birthDate;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    public User(String email, String nickname, String profileImage, UserStatus status) {
        this.email = email;
        this.nickname = nickname;
        this.profileImg = profileImage;
        this.profileImgFromKakao = profileImage;
        this.status = status;
        this.createBaseEntity();
    }

}
