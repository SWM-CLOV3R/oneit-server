package clov3r.api.giftbox.domain.dto;

import clov3r.api.product.domain.status.ProductStatus;
import clov3r.domain.domains.entity.GiftboxProduct;
import clov3r.domain.domains.entity.Keyword;
import clov3r.domain.domains.status.PurchaseStatus;
import clov3r.domain.domains.status.VoteStatus;
import clov3r.domain.domains.type.EmojiName;
import java.util.ArrayList;
import java.util.List;
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
  private ProductStatus productStatus;
  private List<Keyword> keywords = new ArrayList<>();

  // information in giftbox
  private int likeCountInGiftbox;
  private VoteStatus voteStatus;
  private PurchaseStatus purchaseStatus;
  private EmojiName emojiIdx;


  public GiftboxProductDTO(GiftboxProduct giftboxProduct, VoteStatus voteStatus, List<Keyword> keywords) {
    this.idx = giftboxProduct.getProduct().getIdx();
    this.name = giftboxProduct.getProduct().getName();
    this.description = giftboxProduct.getProduct().getDescription();
    this.originalPrice = giftboxProduct.getProduct().getOriginalPrice();
    this.thumbnailUrl = giftboxProduct.getProduct().getThumbnailUrl();
    this.productStatus = giftboxProduct.getProduct().getStatus();
    this.keywords.addAll(keywords);
    this.likeCountInGiftbox = giftboxProduct.getLikeCount();
    this.voteStatus = voteStatus;
    this.purchaseStatus = giftboxProduct.getPurchaseStatus();
    this.emojiIdx = giftboxProduct.getEmojiName();
  }
}
