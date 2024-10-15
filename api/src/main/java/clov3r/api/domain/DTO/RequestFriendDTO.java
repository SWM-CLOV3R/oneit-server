package clov3r.api.domain.DTO;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestFriendDTO {

  Long requestIdx;
  Long fromIdx;
  Long toIdx;
  LocalDateTime createdAt;

}
