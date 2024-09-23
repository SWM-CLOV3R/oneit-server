package clov3r.oneit_server.domain.DTO;

import clov3r.oneit_server.domain.entity.Product;
import lombok.Getter;

@Getter
public class InquiryProductDTO extends ProductSummaryDTO {
  private Long emojiIdx;

  public InquiryProductDTO(Product product, Long emojiIdx) {
    super(product);
    this.emojiIdx = emojiIdx;
  }
}
