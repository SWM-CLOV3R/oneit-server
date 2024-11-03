package clov3r.api.friend.domain.dto;

import clov3r.api.friend.domain.entity.FriendReq;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
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
