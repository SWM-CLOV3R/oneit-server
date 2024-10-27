package clov3r.batch.common.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PushNotificationRequest {
  private String token;
  private String title;
  private String body;
}
