package clov3r.batch.tasklet;


import clov3r.batch.config.kakao.KakaoAlarmResponseDTO;
import clov3r.batch.config.kakao.KakaoAlarmService;
import clov3r.batch.config.kakao.template.TimeAttackTemplate;
import clov3r.batch.repository.BatchRepository;
import clov3r.domain.domains.entity.Friendship;
import clov3r.domain.domains.entity.Notification;
import clov3r.domain.domains.entity.User;
import clov3r.domain.domains.status.NotiStatus;
import clov3r.domain.domains.type.ActionType;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BirthdayNotificationTasklet implements Tasklet {

  private final BatchRepository batchRepository;
  private final KakaoAlarmService kakaoAlarmService;
  private final ApplicationEventPublisher applicationEventPublisher;

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {
    // 7일 후 생일인 사용자를 조회
    sendBirthdayNotification(batchRepository.findUsersWithBirthdayInDays(7), 7);
    // 3일 후 생일인 사용자를 조회
    sendBirthdayNotification(batchRepository.findUsersWithBirthdayInDays(3), 3);
    // 1일 후 생일인 사용자를 조회
    sendBirthdayNotification(batchRepository.findUsersWithBirthdayInDays(1), 1);
    return RepeatStatus.FINISHED;
  }

  public void sendBirthdayNotification(List<User> usersWithUpcomingBirthdays, int days)
      throws Exception {
    // 생일인 사용자의 친구들에게 알림 메시지 전송
    for (User user : usersWithUpcomingBirthdays) {
      List<Friendship> friendsOfBirthdayUser = batchRepository.findByUserIdx(user.getIdx());
      // TODO: 생일자 위시리스트가 없으면 패스
//      if (batchRepository.existsProductLike(user.getIdx())) {
//        continue;
//      }
      for (Friendship friend : friendsOfBirthdayUser) {
        Notification notification = Notification.builder()
            .receiver(friend.getFriend())
            .sender(friend.getUser())
            .actionType(ActionType.BIRTHDAY)
            .platformType("KAKAO")
            .title("생일 알림")
            .body(user.getName() + "님의 생일이 "+ days+"일 남았습니다!")
            .notiStatus(NotiStatus.CREATED)
            .build();
        // 알림 메시지 전송
        TimeAttackTemplate timeAttackTemplate = new TimeAttackTemplate(
            notification,
            new HashMap<>() {{
              put("FRIEND", notification.getSender().getNickname());
              put("DAY", String.valueOf(days));
            }}
        );
        // TODO: 비동기 알림으로 변경
        KakaoAlarmResponseDTO kakaoAlarmResponseDTO = kakaoAlarmService.sendKakaoAlarmTalk(
            notification, timeAttackTemplate, timeAttackTemplate.getArgs());
        if (!kakaoAlarmResponseDTO.getStatus().equals("OK")) {
          log.info("kakao alarm error : {}", kakaoAlarmResponseDTO.getStatus());
          throw new Exception("kakao alarm error");
        }
        notification.sent();
        batchRepository.saveNotification(notification);
      }
    }
  }
}
