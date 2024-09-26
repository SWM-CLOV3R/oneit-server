package clov3r.oneit_server.domain.DTO;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendReqDTO {

  private Long requestIdx;
  private UserDTO fromUser;
  private LocalDateTime requestDate;

  public FriendReqDTO(Long requestIdx, UserDTO fromUser, LocalDateTime requestDate) {
    this.requestIdx = requestIdx;
    this.fromUser = fromUser;
    this.requestDate = requestDate;
  }
}
