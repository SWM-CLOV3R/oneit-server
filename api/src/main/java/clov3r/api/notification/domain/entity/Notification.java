package clov3r.api.notification.domain.entity;

import clov3r.api.auth.domain.entity.User;
import clov3r.api.common.domain.entity.BaseEntity;
import clov3r.api.notification.domain.status.NotiStatus;
import clov3r.api.notification.domain.data.ActionType;
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
import java.time.ZonedDateTime;
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

  @ManyToOne
  @JoinColumn(name = "receiver_idx")
  private User receiver;

  @ManyToOne
  @JoinColumn(name = "sender_idx")
  private User sender;

  @ManyToOne
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
  private ZonedDateTime readAt;

  @Column(name = "sent_at")
  private ZonedDateTime sentAt;

  @Column(name = "noti_status")
  @Enumerated(EnumType.STRING)
  private NotiStatus notiStatus;

}
