package clov3r.api.notification.event;

import clov3r.api.notification.domain.entity.Notification;
import clov3r.api.notification.domain.dto.PushNotificationRequest;
import clov3r.api.common.service.FCMService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {
  private final FCMService fcmService;

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
}
