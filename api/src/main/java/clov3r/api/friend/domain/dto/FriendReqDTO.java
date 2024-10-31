package clov3r.api.friend.domain.dto;

import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendReqDTO {

  private Long requestIdx;
  private FriendDTO fromUser;
  private ZonedDateTime requestDate;

  public FriendReqDTO(Long requestIdx, FriendDTO fromUser, ZonedDateTime requestDate) {
    this.requestIdx = requestIdx;
    this.fromUser = fromUser;
    this.requestDate = requestDate;
  }
}
