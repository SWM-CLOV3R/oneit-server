package clov3r.api.friend.domain.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
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
