package clov3r.oneit_server.domain.DTO;

import clov3r.oneit_server.domain.data.status.VoteStatus;
import clov3r.oneit_server.domain.entity.Product;
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
  private int originalPrice;
  private int currentPrice;
  private int discountRate;
  private String thumbnailUrl;
  private int likeCount;
  private VoteStatus voteStatus;

  public GiftboxProductDTO(Product product, int likeCount, VoteStatus voteStatus) {
    this.idx = product.getIdx();
    this.name = product.getName();
    this.originalPrice = product.getOriginalPrice();
    this.currentPrice = product.getCurrentPrice();
    this.discountRate = product.getDiscountRate();
    this.thumbnailUrl = product.getThumbnailUrl();
    this.likeCount = likeCount;
    this.voteStatus = voteStatus;
  }

}
