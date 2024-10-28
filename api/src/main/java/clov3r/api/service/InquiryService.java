package clov3r.api.service;

import clov3r.api.domain.data.status.InquiryStatus;
import clov3r.api.domain.entity.Giftbox;
import clov3r.api.domain.entity.GiftboxInquiryResult;
import clov3r.api.domain.entity.GiftboxProduct;
import clov3r.api.domain.entity.Inquiry;
import clov3r.api.domain.entity.Notification;
import clov3r.api.domain.entity.Product;
import clov3r.api.domain.request.InquiryRequest;
import clov3r.api.repository.GiftboxProductRepository;
import clov3r.api.repository.GiftboxRepository;
import clov3r.api.repository.InquiryProductRepository;
import clov3r.api.repository.InquiryRepository;
import clov3r.api.repository.NotificationRepository;
import clov3r.api.repository.ProductRepository;
import clov3r.api.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
  private final NotificationRepository notificationRepository;
  private final NotificationService notificationService;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final GiftboxProductRepository giftboxProductRepository;

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

    // send notification
    notificationService.sendInquiryCompleteNotification(inquiryIdx);

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
}
