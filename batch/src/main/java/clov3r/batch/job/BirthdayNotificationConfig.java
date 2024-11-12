package clov3r.batch.job;

import clov3r.batch.tasklet.BirthdayNotificationTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BirthdayNotificationConfig {
  private final JobRepository jobRepository;

  @Bean
  public Job birthdayNotificationJob(PlatformTransactionManager transactionManager, BirthdayNotificationTasklet birthdayNotificationTasklet) {
    return new JobBuilder("birthdayNotificationJob", jobRepository)
            .start(birthdayNotificationStep(transactionManager, birthdayNotificationTasklet))
            .on("FAILED")
            .stopAndRestart(birthdayNotificationStep(transactionManager, birthdayNotificationTasklet))
            .on("*")
            .end()
            .end()
            .build();
  }

  @Bean
  public Step birthdayNotificationStep(PlatformTransactionManager transactionManager,
      BirthdayNotificationTasklet birthdayNotificationTasklet) {
    return new StepBuilder("birthdayNotificationStep", jobRepository)
              .tasklet(birthdayNotificationTasklet, transactionManager)
              .build();}
}
