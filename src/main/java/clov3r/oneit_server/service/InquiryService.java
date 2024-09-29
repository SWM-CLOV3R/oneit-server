package clov3r.oneit_server.service;

import clov3r.oneit_server.domain.data.status.InquiryStatus;
import clov3r.oneit_server.domain.entity.Giftbox;
import clov3r.oneit_server.domain.entity.GiftboxInquiryResult;
import clov3r.oneit_server.domain.entity.Inquiry;
import clov3r.oneit_server.domain.entity.Product;
import clov3r.oneit_server.domain.request.InquiryRequest;
import clov3r.oneit_server.repository.GiftboxRepository;
import clov3r.oneit_server.repository.InquiryProductRepository;
import clov3r.oneit_server.repository.InquiryRepository;
import clov3r.oneit_server.repository.ProductRepository;
import clov3r.oneit_server.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InquiryService {

  private final GiftboxRepository giftboxRepository;
  private final ProductRepository productRepository;
  private final InquiryRepository inquiryRepository;
  private final InquiryProductRepository inquiryProductRepository;
  private final UserRepository userRepository;

  @Transactional
  public Long createInquiry(InquiryRequest inquiryRequest, Long userIdx) {
    Inquiry inquiry = new Inquiry(
        giftboxRepository.findById(inquiryRequest.getGiftboxIdx()),
        userRepository.findByUserIdx(userIdx),
        InquiryStatus.ACTIVE,
        inquiryRequest.getTarget()
    );
    Inquiry saveInquiry = inquiryRepository.save(inquiry);

    return saveInquiry.getIdx();
  }



  @Transactional
  public void completeInquiry(Long inquiryIdx) {

    inquiryRepository.changeInquiryStatus(inquiryIdx, InquiryStatus.COMPLETE);

  }

  @Transactional
  public void createInquiryProduct(Long inquiryIdx, List<Long> productIdxList, Long giftboxIdx) {
    Inquiry inquiry = inquiryRepository.findByIdx(inquiryIdx);
    Giftbox giftbox = giftboxRepository.findById(giftboxIdx);
    for (Long productIdx : productIdxList) {
      Product product = productRepository.findById(productIdx);
      inquiryProductRepository.addProductToInquiry(inquiry, product, giftbox);
    }
  }

  @Transactional
  public void createGiftboxInquiry(Long giftboxIdx, List<Long> productIdxList) {
    Giftbox giftbox = giftboxRepository.findById(giftboxIdx);
    for (Long productIdx : productIdxList) {
      Product product = productRepository.findById(productIdx);
      if (!inquiryProductRepository.existGiftboxAndProduct(giftbox, productIdx)) {
        GiftboxInquiryResult giftboxInquiryResult = new GiftboxInquiryResult(giftbox, product);
        inquiryProductRepository.saveGiftboxInquiryResult(giftboxInquiryResult);
      }
    }
  }
}
