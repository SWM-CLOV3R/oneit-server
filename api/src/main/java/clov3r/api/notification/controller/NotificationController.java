package clov3r.api.notification.controller;

import clov3r.api.auth.repository.UserRepository;
import clov3r.api.auth.service.UserService;
import clov3r.api.common.error.errorcode.CustomErrorCode;
import clov3r.api.common.error.exception.BaseExceptionV2;
import clov3r.api.notification.domain.dto.NotificationDTO;
import clov3r.api.notification.domain.request.SaveDeviceTokenRequest;
import clov3r.api.auth.config.security.Auth;
import clov3r.api.notification.repository.NotificationRepository;
import clov3r.api.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/notification")
public class NotificationController {

  private final NotificationService notificationService;
  private final UserService userService;
  private final UserRepository userRepository;
  private final NotificationRepository notificationRepository;

  @Tag(name = "알림 API", description = "알림 관련 API")
  @Operation(summary = "FCM 토큰 저장", description = "유저 로그인시 알림 허용을 했다면, FCM 토큰을 저장합니다.")
  @PostMapping("/token")
  public void saveDeviceToken(
      @RequestBody SaveDeviceTokenRequest request,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    notificationService.saveDeviceToken(userIdx, request.getDeviceToken(), request.getDeviceType());
  }

  @Tag(name = "알림 API", description = "알림 관련 API")
  @Operation(summary = "알림 리스트 조회", description = "알림 리스트를 조회합니다.")
  @GetMapping("/list")
  public ResponseEntity<List<NotificationDTO>> getNotificationList(
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    List<NotificationDTO> result = notificationService.getNotificationList(userIdx);
    return ResponseEntity.ok(result);
  }

  @Tag(name = "알림 API", description = "알림 관련 API")
  @Operation(summary = "알림 읽음 처리", description = "알림을 읽음 처리합니다.")
  @PatchMapping("/read")
  public void readNotification(
      @RequestParam Long notificationIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    if (userRepository.findByUserIdx(userIdx) == null) {
      throw new BaseExceptionV2(CustomErrorCode.USER_NOT_FOUND);
    }
    if (notificationRepository.fincByIdxAndUserIdx(notificationIdx, userIdx) == null) {
      throw new BaseExceptionV2(CustomErrorCode.NOTIFICATION_NOT_FOUND);
    }
    notificationService.readNotification(notificationIdx);
  }

}
