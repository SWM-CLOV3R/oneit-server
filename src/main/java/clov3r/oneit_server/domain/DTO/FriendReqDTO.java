package clov3r.oneit_server.domain.DTO;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendReqDTO {

  private UserDTO fromUser;
  private LocalDateTime requestDate;

  public FriendReqDTO(UserDTO fromUser, LocalDateTime requestDate) {
    this.fromUser = fromUser;
    this.requestDate = requestDate;
  }
}
