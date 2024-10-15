package clov3r.api.domain.request;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class InviteUserRequest {
  private Long giftboxIdx;
}
