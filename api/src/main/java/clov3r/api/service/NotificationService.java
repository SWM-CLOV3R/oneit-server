package clov3r.api.service;

import clov3r.api.domain.DTO.NotificationDTO;
import clov3r.api.domain.data.ActionType;
import clov3r.api.domain.data.status.NotiStatus;
import clov3r.api.domain.entity.Device;
import clov3r.api.domain.entity.FriendReq;
import clov3r.api.domain.entity.Giftbox;
import clov3r.api.domain.entity.Notification;
import clov3r.api.repository.DeviceRepository;
import clov3r.api.repository.GiftboxRepository;
import clov3r.api.repository.GiftboxUserRepository;
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
  private final GiftboxRepository giftboxRepository;
  private final GiftboxUserRepository giftboxUserRepository;

  @Transactional
  public void saveDeviceToken(Long userIdx, String deviceToken, String deviceType) {
    Device device = deviceRepository.findByUserId(userIdx);
    if (device != null) {
      device.updateLoginAt(LocalDateTime.now());
    } else {
      device = Device.builder()
          .user(userRepository.findByUserIdx(userIdx))
          .deviceToken(deviceToken)
          .deviceType(deviceType)
          .lastLoggedInAt(LocalDateTime.now())
          .build();
      deviceRepository.save(device);
    }
  }

  public Notification sendFriendRequestNotification(FriendReq friendReq) {
    return Notification.builder()
        .receiver(friendReq.getTo())
        .sender(friendReq.getFrom())
        .device(deviceRepository.findByUserId(friendReq.getTo().getIdx()))
        .title("친구 요청")
        .body(friendReq.getFrom().getNickname() + "님이 친구 요청을 보냈습니다.")
        .actionType(ActionType.FRIEND_REQUEST)
        .platformType("FCM")
        .createdAt(LocalDateTime.now())
        .notiStatus(NotiStatus.CREATED)
        .build();
  }

  public Notification sendFriendAcceptanceNotification(FriendReq friendReq) {
    Notification notification = Notification.builder()
        .receiver(friendReq.getFrom())
        .sender(friendReq.getTo())
        .device(deviceRepository.findByUserId(friendReq.getTo().getIdx()))
        .title("친구 요청 수락")
        .body(friendReq.getTo().getNickname() + "님이 친구 요청을 수락했습니다.")
        .actionType(ActionType.FRIEND_ACCEPTANCE)
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

  public Notification sendGiftboxInvitationAcceptanceNotification(Long invitationIdx, Long giftboxIdx, Long userIdx) {
//    List<Long> participants = giftboxRepository.findParticipantsByGiftboxIdx(giftboxIdx);
    Giftbox giftbox = giftboxRepository.findById(giftboxIdx);
    // 선물바구니 참여자들에게 전송
//    for (Long participantIdx : participants) {
//      Notification notification = Notification.builder()
//          .receiver(userRepository.findByUserIdx(participantIdx))
//          .sender(userRepository.findByUserIdx(userIdx))
//          .device(deviceRepository.findByUserId(participantIdx))
//          .title("선물바구니 ["+giftbox.getName()+"] 초대 수락")
//          .body(userRepository.findByUserIdx(userIdx).getNickname() + "님이 ["+giftbox.getName()+"] 선물바구니 초대를 수락했습니다.")
//          .actionType(ActionType.GIFTBOX_ACCEPTANCE)
//          .platformType("FCM")
//          .createdAt(LocalDateTime.now())
//          .notiStatus(NotiStatus.CREATED)
//          .build();
//    }
    // 선물바구니 초대장을 보낸 이에게 전송
    Long invitationSenderIdx = giftboxUserRepository.findSenderByInvitationIdx(invitationIdx);
    Notification notification = Notification.builder()
        .receiver(userRepository.findByUserIdx(invitationSenderIdx))
        .sender(userRepository.findByUserIdx(userIdx))
        .device(deviceRepository.findByUserId(invitationSenderIdx))
        .title("선물바구니 ["+giftbox.getName()+"] 초대 수락")
        .body(userRepository.findByUserIdx(userIdx).getNickname() + "님이 ["+giftbox.getName()+"] 선물바구니 초대를 수락했습니다.")
        .actionType(ActionType.GIFTBOX_ACCEPTANCE)
        .platformType("FCM")
        .createdAt(LocalDateTime.now())
        .notiStatus(NotiStatus.CREATED)
        .build();
    return notification;
  }

  public List<Notification> sendInquiryCompleteNotification(Long inquiryIdx) {
    Giftbox giftbox = giftboxRepository.findByInquiryIdx(inquiryIdx);
    List<Long> participants = giftboxRepository.findParticipantsByGiftboxIdx(giftbox.getIdx());
    // 선물바구니 참여자들에게 전송
    return participants.stream().map(participantIdx -> {
      return Notification.builder()
          .receiver(userRepository.findByUserIdx(participantIdx))
          .device(deviceRepository.findByUserId(participantIdx))
          .title("선물바구니 ["+giftbox.getName()+"] 물어보기 완료")
          .body("선물바구니 ["+giftbox.getName()+"] 에서 받고 싶은 선물 물어보기가 완료되었습니다.")
          .actionType(ActionType.GIFT_ASK_COMPLETE)
          .platformType("FCM")
          .createdAt(LocalDateTime.now())
          .notiStatus(NotiStatus.CREATED)
          .build();
    }).toList();
  }
}
