package clov3r.batch.config.kakao.template;

import clov3r.domain.domains.entity.Notification;
import java.util.HashMap;

public class TimeAttackTemplate extends KakaoAlarmTemplate {
  public TimeAttackTemplate(Notification notification, HashMap<String, String> args) {
    this.templateCode = "10011";
    this.templateName = "위시리스트 알림";
    KakaoButton friendListButton = KakaoButton.builder()
        .name("친구 위시리스트 구경가기")
        .type("WL")
        .url_pc("https://www.oneit.gift/timeattack")
        .url_mobile("https://www.oneit.gift/timeattack")
        .build();
    this.buttons.add(friendListButton);

    this.notification = notification;
    this.args = args;
  }

  @Override
  public String makeMessage(Notification notification, HashMap<String, String> args) {
    this.message = "[ONEIT] 위시리스트 타임어택 오픈!⏰\n"
        + "'"+args.get("FRIEND")+"'님의 생일이 다가오고 있습니다!\n"
        + "이를 대비해 '"+args.get("FRIEND")+"'님의 위시리스트가 한정적으로 오픈됩니다\n"
        + "\n"
        + "늦기 전에 '"+args.get("FRIEND")+"'님의 선물 위시리스트를 확인하고,\n"
        + "마음에 드는 선물을 전달해보세요\uD83E\uDD17\n";
  return this.message;
  }

}
