package clov3r.oneit_server.domain.entity;

import static jakarta.persistence.FetchType.*;

import clov3r.oneit_server.domain.data.GiftboxUserRole;
import clov3r.oneit_server.domain.data.status.InvitationStatus;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private GiftboxUserRole userRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "invitation_status")
    private InvitationStatus invitationStatus;

    public GiftboxUser(Giftbox giftbox, User user, GiftboxUserRole userRole, InvitationStatus invitationStatus) {
        this.giftbox = giftbox;
        this.user = user;
        this.userRole = userRole;
        this.invitationStatus = invitationStatus;

        this.createBaseEntity();
    }

    public GiftboxUser() {

    }
}
