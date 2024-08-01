package clov3r.oneit_server.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "giftbox_user")
public class GiftboxUser extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @OneToOne(fetch = FetchType.LAZY)
    private Giftbox giftbox;
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    private String userRole;

    public GiftboxUser(Giftbox giftbox, User user, String userRole) {
        this.giftbox = giftbox;
        this.user = user;
        this.userRole = userRole;
    }

    public GiftboxUser() {

    }
}
