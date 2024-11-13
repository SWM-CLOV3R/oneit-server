package clov3r.batch.scheduler;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BirthdayNotifyScheduler {

  private final JobLauncher jobLauncher;
  private final Job birthdayNotificationJob;

  // 매일 오전 9시에 실행
  @Scheduled(cron = "0 * 9 * * ?")
  public void runBirthdayNotificationJob() {
    try {
      // JobParameter 객체 생성
      JobParameters jobParameters = new JobParametersBuilder()
          .addString("jobName", "birthdayNotificationJob")  // 예시로 jobName 파라미터 추가
          .addDate("executionTime", new Date())
          .toJobParameters();
      jobLauncher.run(birthdayNotificationJob, jobParameters);
    } catch (Exception e) {
      log.error("Failed to run birthdayNotificationJob", e);
    }
  }

}
