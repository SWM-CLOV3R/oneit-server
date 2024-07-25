package clov3r.oneit_server.domain.entity;

import clov3r.oneit_server.domain.data.Gender;
import jakarta.persistence.*;
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

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_seq_generator", sequenceName = "user_seq", allocationSize = 1)
    private Long idx;
    private String name;
    private String nickname;
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "nickname_from_kakao")
    private String nicknameFromKakao;

    @Column(name = "profile_img_from_kakao")
    private String profileImgFromKakao;


    private Gender gender;
    private String age;
    private Date birthDate;


    private String refreshToken;

}
