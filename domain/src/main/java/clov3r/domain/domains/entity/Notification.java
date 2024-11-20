package clov3r.domain.domains.entity;

import clov3r.domain.domains.status.NotiStatus;
import clov3r.domain.domains.type.ActionType;
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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idx;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "receiver_idx")
  private User receiver;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sender_idx")
  private User sender;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "device_idx")
  private Device device;

  private String title;
  private String body;

  @Column(name = "action_type")
  @Enumerated(EnumType.STRING)
  private ActionType actionType;

  @Column(name = "platform_type")
  private String platformType;

  @Column(name = "read_at")
  private LocalDateTime readAt;

  @Column(name = "sent_at")
  private LocalDateTime sentAt;

  @Column(name = "noti_status")
  @Enumerated(EnumType.STRING)
  private NotiStatus notiStatus;

  public void readNotifitation() {
    this.setReadAt(LocalDateTime.now());
    this.setNotiStatus(NotiStatus.READ);
  }

  public void sent() {
    this.setSentAt(LocalDateTime.now());
    this.setNotiStatus(NotiStatus.SENT);
  }
}
