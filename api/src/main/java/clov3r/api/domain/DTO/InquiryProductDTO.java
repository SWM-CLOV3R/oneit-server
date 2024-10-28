package clov3r.api.domain.DTO;

import clov3r.api.domain.data.EmojiName;
import clov3r.api.domain.entity.Product;
import lombok.Getter;

@Getter
public class InquiryProductDTO extends ProductSummaryDTO {
  private EmojiName emojiName;

  public InquiryProductDTO(Product product, EmojiName emojiName) {
    super(product);
    this.emojiName = emojiName;
  }
}
