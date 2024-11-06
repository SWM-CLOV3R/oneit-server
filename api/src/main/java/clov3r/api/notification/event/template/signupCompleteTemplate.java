package clov3r.api.notification.event.template;

import clov3r.api.notification.domain.dto.kakao.KakaoButton;
import clov3r.domain.domains.entity.Notification;
import java.util.ArrayList;

public class signupCompleteTemplate extends KakaoAlarmTemplate {
  public signupCompleteTemplate() {
    this.templateCode = "10007";
    this.templateName = "회원가입 완료";
    KakaoButton button1 = KakaoButton.builder()
        .name("선물 추천 받기")
        .type("WL")
        .url_pc("https://www.oneit.gift/recommend")
        .url_mobile("https://www.oneit.gift/recommend")
        .build();
    KakaoButton button2 = KakaoButton.builder()
        .name("선물 고르기")
        .type("WL")
        .url_pc("https://www.oneit.gift")
        .url_mobile("https://www.oneit.gift")
        .build();
    this.buttons = new ArrayList<>();
    this.buttons.add(button1);
    this.buttons.add(button2);
  }

  @Override
  public String makeMessage(Notification notification, String customerInquiry) {
    this.message = "[ONEIT] 회원가입 완료\nWANNA GIFT IT, ONE IT \uD83C\uDF89\n안녕하세요. "+notification.getReceiver().getNickname()+"님!\n\n선물을 원하는 당신을 위한 단 하나의 행동 패턴\nONEIT 회원가입이 완료되었습니다!\n친구에게 줄 선물 추천도 받아보고 \uD83C\uDF81\n선물바구니를 만들어 선물 비교도 한 눈에 해보세요! \uD83D\uDD0D";
    return this.message;
  }

}
