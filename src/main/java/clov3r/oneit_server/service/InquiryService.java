package clov3r.oneit_server.service;

import clov3r.oneit_server.domain.data.ProductEmoji;
import clov3r.oneit_server.domain.data.status.InquiryStatus;
import clov3r.oneit_server.domain.entity.Inquiry;
import clov3r.oneit_server.domain.entity.User;
import clov3r.oneit_server.domain.request.InquiryRequest;
import clov3r.oneit_server.repository.InquiryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InquiryService {

  private final InquiryRepository inquiryRepository;

  @Transactional
  public Long createInquiry(InquiryRequest inquiryRequest, Long userIdx) {
    Inquiry inquiry = new Inquiry(
        inquiryRequest.getGiftboxIdx(),
        userIdx,
        InquiryStatus.ACTIVE,
        inquiryRequest.getTarget()
    );
    Inquiry saveInquiry = inquiryRepository.save(inquiry);

    return saveInquiry.getIdx();
  }

  @Transactional
  public void addEmoji(Long inquiryIdx, List<ProductEmoji> productEmojiList) {
    for (ProductEmoji productEmoji : productEmojiList) {
      inquiryRepository.addEmojiToInquiry(inquiryIdx, productEmoji);
    }
  }
}
