package clov3r.batch.job;

import clov3r.batch.common.FcmService;
import clov3r.batch.common.dto.PushNotificationRequest;
import clov3r.batch.domain.data.status.NotiStatus;
import clov3r.batch.domain.entity.Notification;
import clov3r.batch.domain.repository.NotificationRepository;
import clov3r.batch.reader.NotificationDataReader;
import java.time.LocalDateTime;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class SendNotificationBatch {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager platformTransactionManager;
  private final NotificationRepository notificationRepository;
  private final NotificationDataReader reader;
  private final FcmService fcmService;

  @Bean
  public Job sendNotificationJob() {
    return new JobBuilder("sendNotificationJob", jobRepository)
        .start(sendNotificationStep())
        .build();
  }

  @Bean
  public Step sendNotificationStep() {
    System.out.println("SendNotificationBatch.sendNotificationStep");
    return new StepBuilder("sendNotificationStep", jobRepository)
        .<Notification, Notification>chunk(5, platformTransactionManager)
        .reader(sendNotificationReader())
        .processor(sendNotificationProcessor())
        .writer(sendNotificationWriter())
        .build();
  }

  /**
   * 알림 데이터 읽어와서
   * 누구에게, 어떤 메세지를, 어떤 타입으로 보낼지 DTO 생성하기
   * @return
   */
  @Bean
  public RepositoryItemReader<Notification> sendNotificationReader() {
    System.out.println("SendNotificationBatch.sendNotificationReader");
    return new RepositoryItemReaderBuilder<Notification>()
        .methodName("findAll")
        .repository(notificationRepository)
        .sorts(Collections.singletonMap("idx", Sort.Direction.ASC))
        .pageSize(5)
        .name("sendNotificationReader")
        .build();
  }


  @Bean
  public ItemProcessor<Notification, Notification> sendNotificationProcessor() {
    System.out.println("SendNotificationBatch.sendNotificationProcessor");
    return item -> {
      System.out.println("item.getBody() = " + item.getBody() + " : " + item.getCreatedAt());

      // 1. 푸시 알림 전송
      /**
       *  알림 Type에 따라 다른 메세지 전송
       *  1) FCM 서버로 푸시 알림 전송
       *  2) 카카오 알림톡 전송
       */
      PushNotificationRequest notiEvent = PushNotificationRequest.builder()
          .title(item.getTitle())
          .body(item.getBody())
          .build();
      fcmService.sendMessageTo(notiEvent);

      // 2. 알림 전송 후 상태 변경
      item.setSentAt(LocalDateTime.now());
      item.setNotiStatus(NotiStatus.SENT);

      return item;
    };
  }

  /**
   * 전송한 알림 데이터 저장
   * 알림 실패한 경우도 따로 저장 후, 재전송 로직 추가
   * @return
   */
  @Bean
  public RepositoryItemWriter<Notification> sendNotificationWriter() {
    System.out.println("SendNotificationBatch.sendNotificationWriter");
    return new RepositoryItemWriterBuilder<Notification>()
        .methodName("save")
        .repository(notificationRepository)
        .build();
  }

}
