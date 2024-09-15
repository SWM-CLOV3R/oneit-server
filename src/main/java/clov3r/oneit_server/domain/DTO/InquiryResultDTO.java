package clov3r.oneit_server.domain.DTO;

import clov3r.oneit_server.domain.entity.Inquiry;
import java.util.List;
import lombok.AllArgsConstructor;
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