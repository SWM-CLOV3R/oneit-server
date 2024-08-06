package clov3r.oneit_server.domain.entity;

import static jakarta.persistence.FetchType.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "giftbox_idx")
    private Giftbox giftbox;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_idx")
    private User user;
    private String userRole;

    public GiftboxUser(Giftbox giftbox, User user, String userRole) {
        this.giftbox = giftbox;
        this.user = user;
        this.userRole = userRole;

        this.createBaseEntity();
    }

    public GiftboxUser() {

    }
}
