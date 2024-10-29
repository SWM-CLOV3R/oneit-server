package clov3r.api.notification.domain.dto.kakao;

import lombok.Getter;

@Getter
public class KakaoAlarmResponseDTO {
  private String uid;
  private String status;
  private String date;
}
