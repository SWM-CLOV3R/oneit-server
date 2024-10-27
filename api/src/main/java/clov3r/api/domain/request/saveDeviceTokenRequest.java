package clov3r.api.domain.request;

import lombok.Getter;

@Getter
public class saveDeviceTokenRequest {
  private String deviceToken;
  private String deviceType;
}
