package clov3r.api.notification.event;

import static clov3r.domain.error.errorcode.CustomErrorCode.KAKAO_ALARM_ERROR;

import clov3r.domain.error.exception.KakaoException;
import clov3r.api.notification.domain.dto.PushNotificationRequest;
import clov3r.api.common.service.FCMService;
import clov3r.api.notification.domain.dto.kakao.KakaoAlarmResponseDTO;
import clov3r.api.notification.event.template.KakaoAlarmTemplate;
import clov3r.api.notification.service.kakao.KakaoAlarmService;
import clov3r.domain.domains.entity.Notification;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {
  private final FCMService fcmService;
  private final KakaoAlarmService kakaoAlarmService;

  @Async
  @TransactionalEventListener
  public void fcmNotificationEventHandler(Notification notification) throws IOException {
    if (notification.getDevice() == null) {
      return;
    }
    PushNotificationRequest pushNotificationRequest = PushNotificationRequest.builder()
        .token(notification.getDevice().getDeviceToken())
        .title(notification.getTitle())
        .body(notification.getBody())
        .build();
    fcmService.sendMessageTo(pushNotificationRequest);
  }

  @Async
  @TransactionalEventListener
  public void kakaoNotificationEventHandler(KakaoAlarmTemplate kakaoAlarmTemplate) {
    Boolean isAgreeMarketing = kakaoAlarmTemplate.getNotification().getReceiver().getIsAgreeMarketing();
    if (!isAgreeMarketing) {
      return;
    }
    if (kakaoAlarmTemplate.getNotification().getReceiver().getPhoneNumber() == null) {
      return;
    }
    KakaoAlarmResponseDTO kakaoAlarmResponseDTO = kakaoAlarmService.sendKakaoAlarmTalk(
        kakaoAlarmTemplate.getNotification(),
        kakaoAlarmTemplate,
        kakaoAlarmTemplate.getArgs());
    if (!kakaoAlarmResponseDTO.getStatus().equals("OK")) {
      log.info("kakao alarm error : {}", kakaoAlarmResponseDTO.getStatus());
      throw new KakaoException(KAKAO_ALARM_ERROR);
    }

  }
}
