package clov3r.oneit_server.domain.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Builder
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

  public Friendship() {

  }
}
