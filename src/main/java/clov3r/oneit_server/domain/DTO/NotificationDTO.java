package clov3r.oneit_server.domain.DTO;

import clov3r.oneit_server.domain.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
  private Long idx;
  private Long userIdx;
  private String title;
  private String body;
  private String createdAt;

  public NotificationDTO(Notification notification) {
    this.idx = notification.getIdx();
    this.userIdx = notification.getUser().getIdx();
    this.title = notification.getTitle();
    this.body = notification.getBody();
    this.createdAt = notification.getCreatedAt().toString();
  }
}
