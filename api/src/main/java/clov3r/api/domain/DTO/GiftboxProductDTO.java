package clov3r.api.domain.DTO;

import clov3r.api.domain.data.status.ProductStatus;
import clov3r.api.domain.data.status.PurchaseStatus;
import clov3r.api.domain.data.status.VoteStatus;
import clov3r.api.domain.entity.GiftboxProduct;
import clov3r.api.domain.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@Builder
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
  private Long emojiIdx;
  private ProductStatus productStatus;

  public GiftboxProductDTO(GiftboxProduct giftboxProduct, VoteStatus voteStatus) {
    this.idx = giftboxProduct.getProduct().getIdx();
    this.name = giftboxProduct.getProduct().getName();
    this.description = giftboxProduct.getProduct().getDescription();
    this.originalPrice = giftboxProduct.getProduct().getOriginalPrice();
    this.thumbnailUrl = giftboxProduct.getProduct().getThumbnailUrl();
    this.likeCount = giftboxProduct.getLikeCount();
    this.voteStatus = voteStatus;
    this.purchaseStatus = giftboxProduct.getPurchaseStatus();
    this.productStatus = giftboxProduct.getProduct().getStatus();
  }

  public GiftboxProductDTO(Product product) {
    this.idx = product.getIdx();
    this.name = product.getName();
    this.description = product.getDescription();
    this.originalPrice = product.getOriginalPrice();
    this.thumbnailUrl = product.getThumbnailUrl();
  }

}
