package clov3r.domain.domains.entity;

import clov3r.domain.domains.status.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "friendship")
public class Friendship extends BaseEntity {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idx;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_idx")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "friend_idx")
  private User friend;

  @Column(name = "time_attack_alarm")
  private Boolean timeAttackAlarm;

  @Setter
  @Enumerated(EnumType.STRING)
  private Status status;

  public Friendship(User user, User friend) {
    this.user = user;
    this.friend = friend;
    this.status = Status.ACTIVE;
    this.createBaseEntity();
  }

  public void deleteFriendship() {
    this.status = Status.DELETED;
    this.deleteBaseEntity();
  }

  public void changeTimeAttackAlarm() {
    this.timeAttackAlarm = !this.timeAttackAlarm;
  }
}
