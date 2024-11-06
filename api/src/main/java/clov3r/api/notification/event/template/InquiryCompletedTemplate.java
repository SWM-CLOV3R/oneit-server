package clov3r.api.notification.event.template;

import clov3r.api.notification.domain.dto.kakao.KakaoButton;
import clov3r.domain.domains.entity.Notification;
import java.util.HashMap;

public class InquiryCompletedTemplate extends KakaoAlarmTemplate {

  public InquiryCompletedTemplate() {
    this.templateCode = "10010";
    this.templateName = "물어보기 완료 알림";
    KakaoButton giftboxButton = KakaoButton.builder()
        .name("선물바구니 확인하기")
        .type("WL")
        .url_pc("https://www.oneit.gift/basket")
        .url_mobile("https://www.oneit.gift/basket")
        .build();
    this.buttons.add(giftboxButton);
  }

  @Override
  public String makeMessage(Notification notification, HashMap<String, String> args) {
    this.message = "[ONEIT] 선물 취향 응답 완료\uD83D\uDC93\n"
        + "\n"
        + "\'"+args.get("GIFTBOX_NAME")+"\'선물바구니 속 선물 후보들에 대한 물어보기 응답이 완료되었습니다!\n"
        + "\n"
        + "\'"+args.get("GIFTBOX_NAME")+"\'에서 응답 결과를 확인하고\n"
        + "상대방이 받고 싶은 선물 취향을 확인해보세요!\uD83D\uDD0D";
    return this.message;
  }

}
