package clov3r.api.notification.event.template;

import clov3r.api.notification.domain.dto.kakao.KakaoButton;
import clov3r.domain.domains.entity.Notification;
import java.util.HashMap;
import org.aspectj.weaver.ast.Not;

public class FriendAcceptanceTemplate extends KakaoAlarmTemplate {
  public FriendAcceptanceTemplate(Notification notification, HashMap<String, String> args) {
    this.templateCode = "10008";
    this.templateName = "친구 초대 수락 알림";
    KakaoButton friendListButton = KakaoButton.builder()
        .name("친구 목록 확인하기")
        .type("WL")
        .url_pc("https://www.oneit.gift/friends")
        .url_mobile("https://www.oneit.gift/friends")
        .build();
    this.buttons.add(friendListButton);

    this.notification = notification;
    this.args = args;
  }

  @Override
  public String makeMessage(Notification notification, HashMap<String, String> args) {
    this.message = "[ONEIT] 친구 요청 수락\n"
        + "\'"+args.get("FRIEND")+"\'님이 친구 요청을 수락하셨습니다!\n"
        + "\n"
        + "이제 "+args.get("FRIEND")+"님과 서로 위시리스트를 공유하고,"
        + "즐겁게 선물을 주고받을 수 있어요\uD83D\uDE09";
    return this.message;
  }

}
