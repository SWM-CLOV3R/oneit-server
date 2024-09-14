package clov3r.oneit_server.domain.DTO;

import clov3r.oneit_server.domain.entity.Inquiry;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InquiryResultDTO {
  private Long idx;
  private Long giftboxIdx;
  private String target;
  private Long createUserIdx;
  private List<InquiryProductDTO> inquiryProducts;

  public InquiryResultDTO(Inquiry inquiry, List<InquiryProductDTO> inquiryProductDTOS) {
    this.idx = inquiry.getIdx();
    this.giftboxIdx = inquiry.getGiftboxIdx();
    this.target = inquiry.getTarget();
    this.createUserIdx = inquiry.getUserIdx();
    this.inquiryProducts = inquiryProductDTOS;
  }
}