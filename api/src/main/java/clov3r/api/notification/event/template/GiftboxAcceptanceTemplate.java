package clov3r.api.notification.event.template;

import clov3r.api.notification.domain.dto.kakao.KakaoButton;
import clov3r.domain.domains.entity.Notification;
import java.util.HashMap;

public class GiftboxAcceptanceTemplate extends KakaoAlarmTemplate {
  public GiftboxAcceptanceTemplate(Notification notification, HashMap<String, String> args) {
    this.templateCode = "10009";
    this.templateName = "선물바구니 초대 수락 알림";
    KakaoButton giftboxButton = KakaoButton.builder()
        .name("선물바구니 확인하기")
        .type("WL")
        .url_pc("https://www.oneit.gift/basket")
        .url_mobile("https://www.oneit.gift/basket")
        .build();
    this.buttons.add(giftboxButton);

    this.notification = notification;
    this.args = args;
  }

  @Override
  public String makeMessage(Notification notification, HashMap<String, String> args) {
    this.message = "[ONEIT] 선물바구니 초대 수락\uD83D\uDC93\n"
        + '\''+notification.getSender().getNickname()+'\''+"님이"+ '\''+args.get("GIFTBOX_NAME")+'\''+ "선물바구니에 참여하셨습니다!\uD83D\uDE06\n"
        + "\n"
        + "새로운 참여자와 함께 "+ '\''+args.get("GIFTBOX_NAME")+'\''+"에서\n"
        + "다양한 선물들을 고르고 의견을 나누어 보세요\uD83C\uDF81";
    return this.message;
  }

}