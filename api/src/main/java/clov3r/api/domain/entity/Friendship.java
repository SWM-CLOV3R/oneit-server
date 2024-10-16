package clov3r.api.domain.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import clov3r.api.domain.data.status.Status;
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
import lombok.Setter;

@Entity
@Builder
@Getter
@AllArgsConstructor
@Table(name = "friendship")
public class Friendship extends BaseEntity {

  @Id @GeneratedValue(strategy = IDENTITY)
  private Long idx;

  @ManyToOne
  @JoinColumn(name = "user_idx")
  private User user;

  @ManyToOne
  @JoinColumn(name = "friend_idx")
  private User friend;

  @Setter
  @Enumerated(EnumType.STRING)
  private Status status;

  public Friendship() {

  }
}
