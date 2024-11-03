package clov3r.api.friend.domain.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import clov3r.api.common.domain.entity.BaseEntity;
import clov3r.api.auth.domain.entity.User;
import clov3r.api.friend.domain.data.FriendReqStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "friend_req")
public class FriendReq extends BaseEntity {

  @Id @GeneratedValue(strategy = IDENTITY)
  private Long idx;

  @ManyToOne
  @JoinColumn(name = "from_idx")
  private User from;

  @ManyToOne
  @JoinColumn(name = "to_idx")
  private User to;

  @Enumerated(EnumType.STRING)
  private FriendReqStatus friendReqStatus;

  public FriendReq(User from, User to) {
    this.from = from;
    this.to = to;
    this.friendReqStatus = FriendReqStatus.REQUESTED;
    this.createBaseEntity();
  }

  public void accept() {
    this.friendReqStatus = FriendReqStatus.ACCEPTED;
    this.updateBaseEntity();
  }

  public void reject() {
    this.friendReqStatus = FriendReqStatus.REJECTED;
    this.updateBaseEntity();
  }

  public void cancel() {
    this.friendReqStatus = FriendReqStatus.CANCELED;
    this.updateBaseEntity();
  }

}
