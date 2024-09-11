package clov3r.oneit_server.domain.DTO;

import clov3r.oneit_server.domain.entity.Inquiry;
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
    this.giftboxIdx = inquiry.getGiftboxIdx();
    this.target = inquiry.getTarget();
    this.createUserIdx = inquiry.getUserIdx();
    this.selectedProducts = list;
  }
}
