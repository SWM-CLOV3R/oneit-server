package clov3r.api.common.event.template;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationTemplate implements Template {
  FRIEND_REQUEST("친구 요청", "{0}님이 친구 요청을 보냈습니다."),
  FRIEND_ACCEPT("친구 요청 수락", "{0}님이 친구 요청을 수락하였습니다.");

  private final String title;
  private final String body;

  public String getFormattedMessage(String... args) {
    return body.replace("{0}", args[0]);
  }
}
