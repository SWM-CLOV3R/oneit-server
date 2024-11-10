package clov3r.api.notification.event;

import clov3r.api.notification.event.template.KakaoAlarmTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class NotificationPublisher {
  private final ApplicationEventPublisher applicationEventPublisher;

  public NotificationPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  public void publishKakaoNotification(KakaoAlarmTemplate kakaoAlarmTemplate) {
    applicationEventPublisher.publishEvent(kakaoAlarmTemplate);
  }

}
