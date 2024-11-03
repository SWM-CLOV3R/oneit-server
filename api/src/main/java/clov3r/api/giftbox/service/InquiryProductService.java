package clov3r.api.giftbox.service;

import clov3r.api.giftbox.domain.request.ProductEmoji;
import clov3r.api.giftbox.repository.Inquiry.InquiryProductRepository;
import clov3r.api.giftbox.repository.Inquiry.InquiryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InquiryProductService {

  private final InquiryProductRepository inquiryProductRepository;
  private final InquiryRepository inquiryRepository;

  @Transactional
  public void addEmoji(Long inquiryIdx, List<ProductEmoji> productEmojiList) {
    for (ProductEmoji productEmoji : productEmojiList) {
      inquiryProductRepository.addEmojiToInquiry(inquiryIdx, productEmoji);
      inquiryRepository.findByIdx(inquiryIdx).updateBaseEntity();
    }
  }
}
