package clov3r.oneit_server.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_seq_generator", sequenceName = "user_seq", allocationSize = 1)
    private Long idx;
    private String email;
    private String name;
    private String nickname;
    private String phone;
    private String profileImg;

    private String kakaoAccessToken;
    private String kakaoRefreshToken;
    private String accessToken;
    private String refreshToken;
    private String jwt;

}
