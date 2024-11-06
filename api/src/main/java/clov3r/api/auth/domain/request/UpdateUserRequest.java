package clov3r.api.auth.domain.request;

import clov3r.domain.domains.entity.User;
import java.time.LocalDate;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UpdateUserRequest {
  private String nickName;
  private LocalDate birthDate;

  public User toDomain(MultipartFile profileImage) {
    return User.builder()
        .nickname(nickName)
        .birthDate(birthDate)
        .build();
  }
}
