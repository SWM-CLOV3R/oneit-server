package clov3r.api.domain.DTO;

import clov3r.api.domain.data.status.PurchaseStatus;
import clov3r.api.domain.data.status.VoteStatus;
import clov3r.api.domain.entity.Product;
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
  private int currentPrice;
  private int discountRate;
  private String thumbnailUrl;
  private int likeCount;
  private VoteStatus voteStatus;
  private PurchaseStatus purchaseStatus;

  public GiftboxProductDTO(Product product, int likeCount, VoteStatus voteStatus) {
    this.idx = product.getIdx();
    this.name = product.getName();
    this.description = product.getDescription();
    this.originalPrice = product.getOriginalPrice();
    this.currentPrice = product.getCurrentPrice();
    this.discountRate = product.getDiscountRate();
    this.thumbnailUrl = product.getThumbnailUrl();
    this.likeCount = likeCount;
    this.voteStatus = voteStatus;
  }

}
