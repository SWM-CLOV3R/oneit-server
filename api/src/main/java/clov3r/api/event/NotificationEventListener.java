package clov3r.api.event;


import clov3r.api.domain.DTO.PushNotificationRequest;
import clov3r.api.domain.entity.Notification;
import clov3r.api.repository.NotificationRepository;
import clov3r.api.service.common.FCMService;
import java.io.IOException;
import java.time.LocalDateTime;
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
