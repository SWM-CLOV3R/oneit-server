package clov3r.api.notification.event.template;

import clov3r.api.notification.domain.dto.kakao.KakaoButton;
import clov3r.domain.domains.entity.Notification;
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
