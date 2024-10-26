package clov3r.api.domain.request;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class UpdateUserRequest {
  private String nickName;
  private LocalDate birthDate;
}
