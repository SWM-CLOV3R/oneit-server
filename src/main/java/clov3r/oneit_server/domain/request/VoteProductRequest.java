package clov3r.oneit_server.domain.request;

import clov3r.oneit_server.domain.data.status.VoteStatus;
import lombok.Data;

@Data
public class VoteProductRequest {
  private Long giftboxIdx;
  private Long productIdx;
  private String browserUuid;
  private VoteStatus vote;
}
