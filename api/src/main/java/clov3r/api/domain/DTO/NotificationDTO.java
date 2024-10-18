package clov3r.api.domain.DTO;

import clov3r.api.domain.data.status.NotiStatus;
import clov3r.api.domain.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
  private Long idx;
  private Long receiverIdx;
  private String title;
  private String body;
  private String createdAt;
  private NotiStatus notiStatus;

  public NotificationDTO(Notification notification) {
    this.idx = notification.getIdx();
    this.receiverIdx = notification.getReceiver().getIdx();
    this.title = notification.getTitle();
    this.body = notification.getBody();
    this.createdAt = notification.getCreatedAt().toString();
    this.notiStatus = notification.getNotiStatus();
  }
}
