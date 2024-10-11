package clov3r.oneit_server.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idx;

  @ManyToOne
  @JoinColumn(name = "user_idx")
  private User user;
  private String title;
  private String body;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Setter
  private LocalDateTime readAt;



}
