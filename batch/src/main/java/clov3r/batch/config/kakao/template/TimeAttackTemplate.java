package clov3r.batch.config.kakao.template;

import clov3r.domain.domains.entity.Notification;
import java.util.HashMap;

public class TimeAttackTemplate extends KakaoAlarmTemplate {
  public TimeAttackTemplate(Notification notification, HashMap<String, String> args) {
    this.templateCode = "10012";
    this.templateName = "위시리스트 알림";
    KakaoButton friendListButton = KakaoButton.builder()
        .name("친구 위시리스트 구경하기")
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
    this.message = "[ONEIT] 위시리스트 타임어택 오픈 ⏰\n"
        + "'"+args.get("FRIEND")+"'님의 생일까지 D-"+args.get("DAY")+"❗\uFE0F\n"
        + "'"+args.get("FRIEND")+"'님이 받고 싶은 선물을 담아 놓은 위시리스트가 오픈되었습니다\n"
        + "\n"
        + "위시리스트를 확인하고 '"+args.get("FRIEND")+"'님에게 마음에 드는 선물을 전달해보세요\uD83E\uDD17\n";
  return this.message;
  }

}
