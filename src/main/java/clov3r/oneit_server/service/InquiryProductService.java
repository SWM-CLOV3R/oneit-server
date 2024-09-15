package clov3r.oneit_server.service;

import clov3r.oneit_server.domain.data.ProductEmoji;
import clov3r.oneit_server.domain.entity.Giftbox;
import clov3r.oneit_server.repository.InquiryProductRepository;
import clov3r.oneit_server.repository.InquiryRepository;
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
    }
  }

  @Transactional
  public void updateEmojiToGiftbox(Long inquiryIdx, List<ProductEmoji> productEmojiList) {

    Giftbox giftbox = inquiryRepository.findGiftboxByInquiry(inquiryIdx);
    for (ProductEmoji productEmoji : productEmojiList) {
      inquiryProductRepository.updateEmojiToGiftbox(productEmoji, giftbox);
    }

  }
}
