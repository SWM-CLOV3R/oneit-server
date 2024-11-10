package clov3r.batch.config.kakao.template;

import clov3r.domain.domains.entity.Notification;
import java.util.ArrayList;
import java.util.HashMap;
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
  List<KakaoButton> buttons = new ArrayList<>();

  Notification notification;
  HashMap<String, String> args;

  public String makeMessage(Notification notification, HashMap<String, String> args) {
    return this.message;
  }


}
