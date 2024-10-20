package clov3r.api.domain.DTO;

import clov3r.api.domain.entity.Inquiry;
import java.util.List;
import lombok.Getter;

@Getter
public class InquiryDTO {
  private Long idx;
  private Long giftboxIdx;
  private String target;
  private Long createUserIdx;
  private List<ProductSummaryDTO> selectedProducts;

  public InquiryDTO(Inquiry inquiry, List<ProductSummaryDTO> list) {
    this.idx = inquiry.getIdx();
    this.giftboxIdx = inquiry.getGiftbox().getIdx();
    this.target = inquiry.getTarget();
    this.createUserIdx = inquiry.getUser().getIdx();
    this.selectedProducts = list;
  }
}
