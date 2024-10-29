package clov3r.api.common.service.common.kakao;

import clov3r.api.common.domain.DTO.kakao.KakaoAlarmBodyDTO;
import clov3r.api.common.domain.DTO.kakao.KakaoAlarmResponseDTO;
import clov3r.api.common.domain.DTO.kakao.KakaoButton;
import clov3r.api.common.domain.data.kakao.KakaoAlarmTemplate;
import clov3r.api.common.domain.entity.Notification;
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
  @Value("${kakao.talk-dream.customer-inquiry}")
  String customerInquiry;

  public KakaoAlarmResponseDTO sendKakaoAlarmTalk(Notification notification, KakaoAlarmTemplate template) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("authToken", authToken);
    headers.add("serverName", serverName);
    headers.add("paymentType", "P");

    // kakao api server end point URI
    String uri = "https://talkapi.lgcns.com/request/kakao.json";
    List<KakaoButton> buttonList = template.getButtons();
    String message = template.makeMessage(notification, customerInquiry);
    System.out.println("message = " + message);
    KakaoAlarmBodyDTO body = KakaoAlarmBodyDTO.builder()
        .service(serviceId)
        .message(message)
        .mobile(notification.getReceiver().getPhoneNumber())
        .template(String.valueOf(template.getTemplateCode()))
        .buttons(buttonList)
        .build();
    System.out.println("body.toString() = " + body.toString());
    KakaoAlarmResponseDTO kakaoAlarmResponseDTO = restTemplate.exchange(
            uri,
            HttpMethod.POST,
            new HttpEntity<>(body, headers),
            KakaoAlarmResponseDTO.class)
        .getBody();

    return kakaoAlarmResponseDTO;
  }

}
