package clov3r.api.giftbox.domain.request;

import java.util.List;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class InquiryRequest {
  private List<Long> productIdxList;
  private Long giftboxIdx;
  private String target;
}
