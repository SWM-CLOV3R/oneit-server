package clov3r.api.repository;

import static clov3r.api.domain.entity.QEmoji.emoji;
import static clov3r.api.domain.entity.QInquiry.inquiry;
import static clov3r.api.domain.entity.QInquiryProduct.inquiryProduct;

import clov3r.api.domain.data.status.InquiryStatus;
import clov3r.api.domain.entity.Giftbox;
import clov3r.api.domain.entity.Inquiry;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class InquiryRepository {
  private final EntityManager em;
  private final JPAQueryFactory queryFactory;

  public Inquiry save(Inquiry inquiry) {
    em.persist(inquiry);
    em.flush();
    return inquiry;
  }

  public Inquiry findByIdx(Long inquiryIdx) {
    return em.find(Inquiry.class, inquiryIdx);
  }


  public boolean existEmojiById(Long emojiIdx) {
    // check if emoji exists
    return queryFactory
        .select(emoji)
        .from(emoji)
        .where(emoji.idx.eq(emojiIdx))
        .fetchFirst() != null;
  }



  public boolean existInquiry(Long inquiryIdx) {
    return queryFactory
        .select(inquiry)
        .from(inquiry)
        .where(inquiry.idx.eq(inquiryIdx))
        .fetchFirst() != null;
  }

  public boolean existInquiryProduct(Long inquiryIdx, Long productIdx) {
    return queryFactory
        .select(inquiryProduct)
        .from(inquiryProduct)
        .where(inquiryProduct.inquiry.idx.eq(inquiryIdx)
            .and(inquiryProduct.product.idx.eq(productIdx)))
        .fetch() != null;
  }

  public void changeInquiryStatus(Long inquiryIdx, InquiryStatus inquiryStatus) {
    queryFactory
        .update(inquiry)
        .set(inquiry.inquiryStatus, inquiryStatus)
        .where(inquiry.idx.eq(inquiryIdx))
        .execute();
  }

  public void updatedAt(Long inquiryIdx) {
    // 이미 created

  }

  public Giftbox findGiftboxByInquiry(Long inquiryIdx) {
    return queryFactory
        .select(inquiry.giftbox)
        .from(inquiry)
        .where(inquiry.idx.eq(inquiryIdx))
        .fetchFirst();
  }

}
