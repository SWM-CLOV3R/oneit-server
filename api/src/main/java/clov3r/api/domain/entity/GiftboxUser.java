package clov3r.api.domain.entity;

import static jakarta.persistence.FetchType.LAZY;

import clov3r.api.domain.data.GiftboxUserRole;
import clov3r.api.domain.data.status.InvitationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
