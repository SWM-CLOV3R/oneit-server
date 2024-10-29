package clov3r.api.giftbox.domain.request;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class InviteUserRequest {
  private Long giftboxIdx;
}
