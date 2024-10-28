package clov3r.api.common.service;

import static clov3r.api.common.error.errorcode.CustomErrorCode.KAKAO_ALARM_ERROR;

import clov3r.api.common.domain.DTO.NotificationDTO;
import clov3r.api.common.domain.DTO.kakao.KakaoAlarmResponseDTO;
import clov3r.api.common.domain.data.ActionType;
import clov3r.api.common.domain.data.kakao.signupCompleteTemplate;
import clov3r.api.common.domain.data.status.NotiStatus;
import clov3r.api.common.domain.entity.Device;
import clov3r.api.common.domain.entity.FriendReq;
import clov3r.api.common.repository.DeviceRepository;
import clov3r.api.common.repository.NotificationRepository;
import clov3r.api.common.repository.UserRepository;
import clov3r.api.common.service.common.kakao.KakaoAlarmService;
import clov3r.api.giftbox.domain.entity.Giftbox;
import clov3r.api.common.domain.entity.Notification;
import clov3r.api.common.domain.entity.User;
import clov3r.api.common.error.exception.KakaoException;
import clov3r.api.giftbox.repository.GiftboxRepository;
import clov3r.api.giftbox.repository.GiftboxUserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
  private final KakaoAlarmService kakaoAlarmService;
  private final ApplicationEventPublisher applicationEventPublisher;

  /**
   * 디바이스 토큰 저장
   * @param userIdx
   * @param deviceToken
   * @param deviceType
   */
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

  /**
   * 알림 리스트 조회
   * @param userIdx
   * @return
   */
  public List<NotificationDTO> getNotificationList(Long userIdx) {

    List<Notification> notificationList= notificationRepository.findAllByUserId(userIdx);
    return notificationList.stream()
        .map(NotificationDTO::new)
        .toList();
  }

  /**
   * 알림 읽음 처리
   * @param notificationIdx
   */
  @Transactional
  public void readNotification(Long notificationIdx) {

    Notification notification = notificationRepository.findById(notificationIdx).orElseThrow();
    notification.setReadAt(LocalDateTime.now());
    notification.setNotiStatus(NotiStatus.READ);
    notificationRepository.save(notification);
  }

  /**
   * 친구 요청 알림
   * FCM 전송
   * @param friendReq
   */
  public void sendFriendRequestNotification(FriendReq friendReq) {
    Notification notification =  Notification.builder()
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
    applicationEventPublisher.publishEvent(notification);
    notificationRepository.save(notification);
  }

  /**
   * 친구 요청 수락 알림
   * FCM 전송
   * @param friendReq
   * @return
   */
  public Notification sendFriendAcceptanceNotification(FriendReq friendReq) {
    Notification notification = Notification.builder()
        .receiver(friendReq.getFrom())
        .sender(friendReq.getTo())
        .device(deviceRepository.findByUserId(friendReq.getFrom().getIdx()))
        .title("친구 요청 수락")
        .body(friendReq.getTo().getNickname() + "님이 친구 요청을 수락했습니다.")
        .actionType(ActionType.FRIEND_ACCEPTANCE)
        .createdAt(LocalDateTime.now())
        .notiStatus(NotiStatus.CREATED)
        .build();
    applicationEventPublisher.publishEvent(notification);
    notificationRepository.save(notification);
    return notification;
  }

  /**
   * 선물바구니 초대 수락 알림
   * FCM 전송
   * @param invitationIdx
   * @param giftboxIdx
   * @param userIdx
   * @return
   */
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
    applicationEventPublisher.publishEvent(notification);
    notificationRepository.save(notification);
    return notification;
  }

  /**
   * 선물바구니 초대 수락 알림
   * FCM 전송
   * @param inquiryIdx
   */
  public void sendInquiryCompleteNotification(Long inquiryIdx) {
    Giftbox giftbox = giftboxRepository.findByInquiryIdx(inquiryIdx);
    List<Long> participants = giftboxRepository.findParticipantsByGiftboxIdx(giftbox.getIdx());
    // 선물바구니 참여자들에게 전송
    List<Notification> notificationList =  participants.stream().map(participantIdx -> {
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
    for (Notification notification : notificationList) {
      applicationEventPublisher.publishEvent(notification);
      notificationRepository.save(notification);
    }
  }

  /**
   * 회원가입 완료 알림
   * 카카오톡 알림 전송
   * @param user
   */
  public void sendSignupCompleteNotification(User user) {
    Notification notification =  Notification.builder()
        .receiver(user)
        .build();

    signupCompleteTemplate signupComplete = new signupCompleteTemplate();
    KakaoAlarmResponseDTO kakaoAlarmResponseDTO = kakaoAlarmService.sendKakaoAlarmTalk(
        notification, signupComplete);
    if (!kakaoAlarmResponseDTO.getStatus().equals("OK")) {
      throw new KakaoException(KAKAO_ALARM_ERROR);
    }
    notificationRepository.save(notification);
  }

}
