package clov3r.api.giftbox.domain.request;

import clov3r.domain.domains.status.VoteStatus;
import lombok.Data;

@Data
public class VoteProductRequest {
  private Long giftboxIdx;
  private Long productIdx;
  private String browserUuid;
  private VoteStatus vote;
}
