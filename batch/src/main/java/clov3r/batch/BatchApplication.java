package clov3r.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(
    scanBasePackages = "clov3r.batch"
)
public class BatchApplication {

  public static void main(String[] args) {
    SpringApplication.run(BatchApplication.class, args);
  }

}
