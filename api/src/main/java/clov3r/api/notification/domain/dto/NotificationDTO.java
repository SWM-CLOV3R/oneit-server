package clov3r.api.notification.domain.dto;

import clov3r.api.notification.domain.data.ActionType;
import clov3r.api.notification.domain.status.NotiStatus;
import clov3r.api.notification.domain.entity.Notification;
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
  private ActionType actionType;

  public NotificationDTO(Notification notification) {
    this.idx = notification.getIdx();
    this.receiverIdx = notification.getReceiver().getIdx();
    this.title = notification.getTitle();
    this.body = notification.getBody();
    this.createdAt = notification.getCreatedAt().toString();
    this.notiStatus = notification.getNotiStatus();
    this.actionType = notification.getActionType();
  }
}
