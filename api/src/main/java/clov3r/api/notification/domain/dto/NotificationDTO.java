package clov3r.api.notification.domain.dto;

import clov3r.domain.domains.entity.Notification;
import clov3r.domain.domains.status.NotiStatus;
import clov3r.domain.domains.type.ActionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
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

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
  private LocalDateTime createdAt;

  private NotiStatus notiStatus;
  private ActionType actionType;

  public NotificationDTO(Notification notification) {
    this.idx = notification.getIdx();
    if (notification.getReceiver() != null) {
      this.receiverIdx = notification.getReceiver().getIdx();
    } else {
      this.receiverIdx = null;
    }
    this.title = notification.getTitle();
    this.body = notification.getBody();
    this.createdAt = notification.getCreatedAt();
    this.notiStatus = notification.getNotiStatus();
    this.actionType = notification.getActionType();
  }
}
