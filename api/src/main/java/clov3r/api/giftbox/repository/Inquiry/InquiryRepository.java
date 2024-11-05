package clov3r.api.giftbox.repository.Inquiry;

import static clov3r.domain.domains.entity.QEmoji.emoji;
import static clov3r.domain.domains.entity.QInquiry.inquiry;
import static clov3r.domain.domains.entity.QInquiryProduct.inquiryProduct;

import clov3r.domain.domains.entity.Giftbox;
import clov3r.domain.domains.entity.Inquiry;
import clov3r.domain.domains.status.InquiryStatus;
import clov3r.domain.domains.status.Status;
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
    return queryFactory.select(inquiry)
        .from(inquiry)
        .where(inquiry.idx.eq(inquiryIdx))
        .where(inquiry.giftbox.status.eq(Status.ACTIVE)
            .or(inquiry.giftbox.status.ne(Status.DELETED)))
        .fetchFirst();
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
