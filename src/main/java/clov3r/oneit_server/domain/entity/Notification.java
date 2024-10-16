package clov3r.oneit_server.domain.entity;

import clov3r.oneit_server.domain.data.status.NotiStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
  @JoinColumn(name = "receiver_idx")
  private User receiver;

  @ManyToOne
  @JoinColumn(name = "sender_idx")
  private User sender;

  private String title;
  private String body;

  @Column(name = "created_at")
  private LocalDateTime createdAt;
  @Setter
  private LocalDateTime readAt;
  @Setter
  private LocalDateTime sentAt;

  @Setter
  @Column(name = "noti_status")
  @Enumerated(EnumType.STRING)
  private NotiStatus notiStatus;
}
