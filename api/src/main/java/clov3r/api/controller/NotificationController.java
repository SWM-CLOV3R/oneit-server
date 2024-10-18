package clov3r.api.controller;

import clov3r.api.config.security.Auth;
import clov3r.api.domain.DTO.NotificationDTO;
import clov3r.api.service.FCMService;
import clov3r.api.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/notification")
public class NotificationController {

  private final FCMService fcmService;
  private final NotificationService notificationService;

  @Tag(name = "알림 API", description = "알림 관련 API")
  @Operation(summary = "FCM 토큰 저장", description = "유저 로그인시 알림 허용을 했다면, FCM 토큰을 저장합니다.")
  @GetMapping("/token")   // TODO: POST로 변경, token을 body에 담아서 입력받기, device type도 함께 입력받기
  public void sendFCMNotification(
      @RequestParam String token,
      @Parameter(hidden = true) @Auth Long userIdx
  ) throws IOException {
    String title = "FCM TEST";
    String body = "안녕하십니까!";
    fcmService.sendMessageTo(token, title, body);
    notificationService.saveDeviceToken(userIdx, token);
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
      @RequestParam Long notificationIdx
  ) {
    notificationService.readNotification(notificationIdx);
  }

}
