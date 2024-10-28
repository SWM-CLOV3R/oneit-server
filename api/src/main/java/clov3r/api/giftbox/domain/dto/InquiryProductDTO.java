package clov3r.api.giftbox.domain.dto;

import clov3r.api.common.domain.DTO.ProductSummaryDTO;
import clov3r.api.common.domain.entity.Product;
import clov3r.api.giftbox.domain.data.EmojiName;
import lombok.Getter;

@Getter
public class InquiryProductDTO extends ProductSummaryDTO {
  private EmojiName emojiName;

  public InquiryProductDTO(Product product, EmojiName emojiName) {
    super(product);
    this.emojiName = emojiName;
  }
}
