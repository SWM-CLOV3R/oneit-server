package clov3r.api.domain.DTO;

import clov3r.api.domain.data.status.PurchaseStatus;
import clov3r.api.domain.data.status.VoteStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GiftboxProductDTO {
  private Long idx;
  private String name;
  private String description;
  private int originalPrice;
//  private int currentPrice;
//  private int discountRate;
  private String thumbnailUrl;
  private int likeCount;
  private VoteStatus voteStatus;
  private PurchaseStatus purchaseStatus;

}
