package clov3r.oneit_server.service;

import clov3r.oneit_server.domain.DTO.NotificationDTO;
import clov3r.oneit_server.domain.entity.Device;
import clov3r.oneit_server.domain.entity.FriendReq;
import clov3r.oneit_server.domain.entity.Notification;
import clov3r.oneit_server.repository.DeviceRepository;
import clov3r.oneit_server.repository.NotificationRepository;
import clov3r.oneit_server.repository.UserRepository;
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

  public void sendFreindRequestNotification(FriendReq friendReq) {
    Notification notification = Notification.builder()
        .user(friendReq.getTo())
        .title("친구 요청")
        .body(friendReq.getFrom().getNickname() + "님이 친구 요청을 보냈습니다.")
        .createdAt(LocalDateTime.now())
        .build();
    notificationRepository.save(notification);
  }

  public void sendFriendAcceptanceNotification(FriendReq friendReq) {
    Notification notification = Notification.builder()
        .user(friendReq.getFrom())
        .title("친구 요청 수락")
        .body(friendReq.getTo().getNickname() + "님이 친구 요청을 수락했습니다.")
        .createdAt(LocalDateTime.now())
        .build();
    notificationRepository.save(notification);
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
    notificationRepository.save(notification);
  }
}
