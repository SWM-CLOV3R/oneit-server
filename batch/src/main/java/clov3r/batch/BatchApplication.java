package clov3r.batch;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {
    "clov3r.batch",
    "clov3r.api.auth.repository",
    "clov3r.api.notification.service.kakao",
    "clov3r.api.friend.repository",
    "clov3r.api.notification.service.kakao",
})
@EntityScan(basePackages = {"clov3r.domain.domains.entity"})
@EnableScheduling
public class BatchApplication {

  public static void main(String[] args) {
    SpringApplication.run(BatchApplication.class, args);
  }

  @PostConstruct
  public void init() {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
  }

}
