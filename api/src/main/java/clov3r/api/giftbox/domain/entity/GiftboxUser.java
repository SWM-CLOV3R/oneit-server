package clov3r.api.giftbox.domain.entity;

import static jakarta.persistence.FetchType.LAZY;

import clov3r.api.giftbox.domain.data.GiftboxUserRole;
import clov3r.api.giftbox.domain.status.InvitationStatus;
import clov3r.api.common.domain.entity.BaseEntity;
import clov3r.api.auth.domain.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Entity
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "sender_idx")
    private User sender;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private GiftboxUserRole userRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "invitation_status")
    private InvitationStatus invitationStatus;

    public GiftboxUser() {
        this.createBaseEntity();
    }

    public void createInvitation(Giftbox giftbox, User user) {
        this.giftbox = giftbox;
        this.user = null;
        this.sender = user;
        this.userRole = GiftboxUserRole.PARTICIPANT;
        this.invitationStatus = InvitationStatus.PENDING;
    }
    public void createAcceptInvitation(GiftboxUser gu, User user) {
        this.giftbox = gu.getGiftbox();
        this.user = user;
        this.sender = gu.getSender();
        this.userRole = GiftboxUserRole.PARTICIPANT;
        this.invitationStatus = InvitationStatus.ACCEPTED;
    }
    public void createManager(Giftbox giftbox, User user) {
        this.giftbox = giftbox;
        this.user = user;
        this.userRole = GiftboxUserRole.MANAGER;
        this.invitationStatus = InvitationStatus.ACCEPTED;
    }
}
