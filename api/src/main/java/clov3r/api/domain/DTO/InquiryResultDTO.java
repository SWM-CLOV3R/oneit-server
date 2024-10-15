package clov3r.api.domain.DTO;

import java.util.List;
import lombok.Getter;

@Getter
public class InquiryResultDTO {
  private Long giftboxIdx;
  private List<InquiryProductDTO> inquiryProducts;

  public InquiryResultDTO(Long giftboxIdx, List<InquiryProductDTO> inquiryProductDTOS) {
    this.giftboxIdx = giftboxIdx;
    this.inquiryProducts = inquiryProductDTOS;
  }
}