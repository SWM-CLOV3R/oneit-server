package clov3r.api.domain.DTO;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendReqDTO {

  private Long requestIdx;
  private FriendDTO fromUser;
  private LocalDateTime requestDate;

  public FriendReqDTO(Long requestIdx, FriendDTO fromUser, LocalDateTime requestDate) {
    this.requestIdx = requestIdx;
    this.fromUser = fromUser;
    this.requestDate = requestDate;
  }
}
