package clov3r.api.giftbox.domain.dto;

import clov3r.api.common.domain.DTO.ProductDetailDTO;
import clov3r.api.common.domain.data.status.LikeStatus;
import clov3r.api.common.domain.data.status.PurchaseStatus;
import clov3r.api.common.domain.entity.Product;
import clov3r.api.giftbox.domain.data.EmojiName;
import clov3r.api.giftbox.domain.entity.GiftboxProduct;
import clov3r.api.giftbox.domain.status.VoteStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GiftboxProductDetailDTO extends ProductDetailDTO {
  // information in giftbox
  private int likeCountInGiftbox;
  private VoteStatus voteStatus;
  private PurchaseStatus purchaseStatus;
  private EmojiName emojiIdx;

  public GiftboxProductDetailDTO(Product product, GiftboxProduct giftboxProduct, VoteStatus voteStatus, LikeStatus likeStatus) {
    super(product, likeStatus);
    this.likeCountInGiftbox = giftboxProduct.getLikeCount();
    this.voteStatus = voteStatus;
    this.purchaseStatus = giftboxProduct.getPurchaseStatus();
    this.emojiIdx = giftboxProduct.getEmojiName();
  }
}
