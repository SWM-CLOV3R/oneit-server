package clov3r.batch.schedule;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
public class NotiSchedule {

  private final JobLauncher jobLauncher;
  private final JobRegistry jobRegistry;

  // 10분마다 실행
  @Scheduled(cron = "0 0/10 * * * *", zone = "Asia/Seoul")
  public void sendNotification() throws Exception {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
//    String date = dateFormat.format(LocalDateTime.now());

    JobParameters jobParameters = new JobParametersBuilder()
        .addString("date", LocalDateTime.now().toString())
        .toJobParameters();

    jobLauncher.run(jobRegistry.getJob("sendNotificationJob"), jobParameters);
  }

}
