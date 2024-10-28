package clov3r.api.common.domain.data.kakao;

import clov3r.api.common.domain.entity.Notification;
import clov3r.api.common.domain.DTO.kakao.KakaoButton;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoAlarmTemplate {

  String templateCode;
  String templateName;
  String message;
  List<KakaoButton> buttons;

  public String makeMessage(Notification notification, String customerInquiry) {
    return this.message;
  }


}
