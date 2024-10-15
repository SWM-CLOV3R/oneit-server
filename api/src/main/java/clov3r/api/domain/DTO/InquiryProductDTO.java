package clov3r.api.domain.DTO;

import clov3r.api.domain.entity.Product;
import lombok.Getter;

@Getter
public class InquiryProductDTO extends ProductSummaryDTO {
  private Long emojiIdx;

  public InquiryProductDTO(Product product, Long emojiIdx) {
    super(product);
    this.emojiIdx = emojiIdx;
  }
}
