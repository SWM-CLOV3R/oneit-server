package clov3r.api.notification.domain.request;

import lombok.Getter;

@Getter
public class SaveDeviceTokenRequest {
  private String deviceToken;
  private String deviceType;
}
