package clov3r.api.common.config;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import org.springframework.stereotype.Component;

@Component
public class TimeZoneConfig {

  @PostConstruct
  public void init() {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
  }
}