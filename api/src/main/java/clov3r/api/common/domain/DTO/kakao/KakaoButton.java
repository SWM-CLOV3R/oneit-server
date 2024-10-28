package clov3r.api.common.domain.DTO.kakao;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoButton {
  private String name;
  private String type;
  private String url_pc;
  private String url_mobile;
}
