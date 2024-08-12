package clov3r.oneit_server.domain.request;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class InviteUserRequest {
  private Long giftboxIdx;
}
