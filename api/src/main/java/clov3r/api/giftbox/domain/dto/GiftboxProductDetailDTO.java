package clov3r.api.giftbox.domain.dto;

import clov3r.api.product.domain.dto.ProductDetailDTO;
import clov3r.domain.domains.entity.GiftboxProduct;
import clov3r.domain.domains.entity.Product;
import clov3r.domain.domains.status.PurchaseStatus;
import clov3r.domain.domains.status.VoteStatus;
import clov3r.domain.domains.type.EmojiName;
import java.util.List;
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

  public GiftboxProductDetailDTO(Product product, GiftboxProduct giftboxProduct, VoteStatus voteStatus, List<String> detailImages) {
    super(product, null, detailImages);
    this.likeCountInGiftbox = giftboxProduct.getLikeCount();
    this.voteStatus = voteStatus;
    this.purchaseStatus = giftboxProduct.getPurchaseStatus();
    this.emojiIdx = giftboxProduct.getEmojiName();
  }
}
