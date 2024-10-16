package clov3r.api.domain.DTO;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PushNotificationRequest {
  private String token;
  private String title;
  private String body;
}
