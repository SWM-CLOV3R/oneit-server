package clov3r.api.common.domain.request;

import lombok.Getter;

@Getter
public class SaveDeviceTokenRequest {
  private String deviceToken;
  private String deviceType;
}
