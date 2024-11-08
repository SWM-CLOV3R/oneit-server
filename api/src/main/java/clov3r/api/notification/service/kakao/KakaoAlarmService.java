package clov3r.api.notification.service.kakao;

import clov3r.api.notification.domain.dto.kakao.KakaoAlarmBodyDTO;
import clov3r.api.notification.domain.dto.kakao.KakaoAlarmResponseDTO;
import clov3r.api.notification.domain.dto.kakao.KakaoButton;
import clov3r.api.notification.event.template.KakaoAlarmTemplate;
import clov3r.domain.domains.entity.Notification;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoAlarmService {

  private final RestTemplate restTemplate;

  @Value("${kakao.talk-dream.auth-token}")
  String authToken;
  @Value("${kakao.talk-dream.server-name}")
  String serverName;
  @Value("${kakao.talk-dream.service-id}")
  long serviceId;

  public KakaoAlarmResponseDTO sendKakaoAlarmTalk(Notification notification, KakaoAlarmTemplate template, HashMap<String, String> args) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("authToken", authToken);
    headers.add("serverName", serverName);
    headers.add("paymentType", "P");

    // kakao api server end point URI
    String uri = "https://talkapi.lgcns.com/request/kakao.json";
    List<KakaoButton> buttonList = template.getButtons();
    String message = template.makeMessage(notification, args);
    KakaoAlarmBodyDTO body = KakaoAlarmBodyDTO.builder()
        .service(serviceId)
        .message(message)
        .mobile(notification.getReceiver().getPhoneNumber())
        .template(String.valueOf(template.getTemplateCode()))
        .buttons(buttonList)
        .build();
    KakaoAlarmResponseDTO kakaoAlarmResponseDTO = restTemplate.exchange(
            uri,
            HttpMethod.POST,
            new HttpEntity<>(body, headers),
            KakaoAlarmResponseDTO.class)
        .getBody();

    return kakaoAlarmResponseDTO;
  }

}
