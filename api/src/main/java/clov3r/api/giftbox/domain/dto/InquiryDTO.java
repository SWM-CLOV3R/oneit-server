package clov3r.api.giftbox.domain.dto;

import clov3r.api.product.domain.dto.ProductSummaryDTO;
import clov3r.api.giftbox.domain.entity.Inquiry;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InquiryDTO {
  private Long idx;
  private Long giftboxIdx;
  private String target;
  private List<ProductSummaryDTO> selectedProducts;

  public InquiryDTO(Inquiry inquiry, List<ProductSummaryDTO> list) {
    this.idx = inquiry.getIdx();
    this.giftboxIdx = inquiry.getGiftbox().getIdx();
    this.target = inquiry.getTarget();
    this.selectedProducts = list;
  }
}
