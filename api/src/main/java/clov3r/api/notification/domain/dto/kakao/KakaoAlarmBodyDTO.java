package clov3r.api.notification.domain.dto.kakao;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KakaoAlarmBodyDTO {
  private long service;
  private String message;
  private String mobile;
  private String template;
  private List<KakaoButton> buttons;

  @Override
  public String toString() {
    return "KakaoAlarmBodyDTO{" +
        "service=" + service +
        ", message='" + message + '\'' +
        ", mobile='" + mobile + '\'' +
        ", template='" + template + '\'' +
        ", buttons=" + buttons +
        '}';
  }

}
