package clov3r.api.service;

import clov3r.api.domain.DTO.NotificationDTO;
import clov3r.api.domain.data.status.NotiStatus;
import clov3r.api.domain.entity.Device;
import clov3r.api.domain.entity.FriendReq;
import clov3r.api.domain.entity.Notification;
import clov3r.api.repository.DeviceRepository;
import clov3r.api.repository.NotificationRepository;
import clov3r.api.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;
  private final DeviceRepository deviceRepository;

  @Transactional
  public void saveDeviceToken(Long userIdx, String token) {
    Device device = deviceRepository.findByUserId(userIdx);
    if (device != null) {
      device.updateLoginAt(LocalDateTime.now());
    } else {
      device = Device.builder()
          .user(userRepository.findByUserIdx(userIdx))
          .deviceToken(token)
          .lastLoggedInAt(LocalDateTime.now())
          .build();
      deviceRepository.save(device);
    }
  }

  public Notification sendFriendRequestNotification(FriendReq friendReq) {
    Notification notification = Notification.builder()
        .receiver(friendReq.getTo())
        .sender(friendReq.getFrom())
        .device(deviceRepository.findByUserId(friendReq.getTo().getIdx()))
        .title("친구 요청")
        .body(friendReq.getFrom().getNickname() + "님이 친구 요청을 보냈습니다.")
        .actionType("FRIEND_REQUEST")
        .platformType("FCM")
        .createdAt(LocalDateTime.now())
        .notiStatus(NotiStatus.CREATED)
        .build();
    return notification;
  }

  public Notification sendFriendAcceptanceNotification(FriendReq friendReq) {
    Notification notification = Notification.builder()
        .receiver(friendReq.getFrom())
        .sender(friendReq.getTo())
        .device(deviceRepository.findByUserId(friendReq.getTo().getIdx()))
        .title("친구 요청 수락")
        .body(friendReq.getTo().getNickname() + "님이 친구 요청을 수락했습니다.")
        .actionType("FRIEND_ACCEPT")
        .createdAt(LocalDateTime.now())
        .notiStatus(NotiStatus.CREATED)
        .build();
    notificationRepository.save(notification);
    return notification;
  }

  public List<NotificationDTO> getNotificationList(Long userIdx) {

    List<Notification> notificationList= notificationRepository.findAllByUserId(userIdx);
    return notificationList.stream()
        .map(NotificationDTO::new)
        .toList();
  }

  @Transactional
  public void readNotification(Long notificationIdx) {

    Notification notification = notificationRepository.findById(notificationIdx).orElseThrow();
    notification.setReadAt(LocalDateTime.now());
    notification.setNotiStatus(NotiStatus.READ);
    notificationRepository.save(notification);
  }
}
