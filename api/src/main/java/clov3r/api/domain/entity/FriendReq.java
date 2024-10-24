package clov3r.api.domain.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import clov3r.api.domain.data.status.FriendReqStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Builder
@Getter
@AllArgsConstructor
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

  public void accept() {
    this.friendReqStatus = FriendReqStatus.ACCEPTED;
  }

  public void reject() {
    this.friendReqStatus = FriendReqStatus.REJECTED;
  }

  public FriendReq() {

  }
}
