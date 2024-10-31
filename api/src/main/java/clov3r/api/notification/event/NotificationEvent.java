package clov3r.api.notification.event;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NotificationEvent {
  private String receiverNickname;
  private String deviceToken;
  private String phoneNumber;
  private String title;
  private String body;

}
