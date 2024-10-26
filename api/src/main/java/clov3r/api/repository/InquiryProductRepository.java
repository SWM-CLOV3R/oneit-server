package clov3r.api.repository;

import static clov3r.api.domain.entity.QGiftboxInquiryResult.giftboxInquiryResult;
import static clov3r.api.domain.entity.QInquiryProduct.inquiryProduct;

import clov3r.api.domain.data.ProductEmoji;
import clov3r.api.domain.entity.Giftbox;
import clov3r.api.domain.entity.GiftboxInquiryResult;
import clov3r.api.domain.entity.Inquiry;
import clov3r.api.domain.entity.InquiryProduct;
import clov3r.api.domain.entity.Product;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class InquiryProductRepository {


    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    private final InquiryRepository inquiryRepository;


    public void addProductToInquiry(Inquiry inquiry, Product product, Giftbox giftbox) {
        InquiryProduct inquiryProduct = new InquiryProduct(
            inquiry,
            product,
            giftbox
        );
        em.persist(inquiryProduct);
    }

    public List<Product> findProductListByInquiry(Inquiry inquiry) {
        return queryFactory
            .select(inquiryProduct.product)
            .from(inquiryProduct)
            .where(inquiryProduct.inquiry.eq(inquiry))
            .fetch();
    }


    public void addEmojiToInquiry(Long inquiryIdx, ProductEmoji productEmoji) {
        queryFactory
            .update(inquiryProduct)
            .set(inquiryProduct.emojiName, productEmoji.getEmojiName())
            .set(inquiryProduct.updatedAt, LocalDateTime.now())
            .where(inquiryProduct.inquiry.idx.eq(inquiryIdx)
                .and(inquiryProduct.product.idx.eq(productEmoji.getProductIdx())))
            .execute();
    }


    public Long findEmojiByInquiryAndProduct(Long inquiryIdx, Long productIdx) {
        return queryFactory
            .select(inquiryProduct.emojiIdx)
            .from(inquiryProduct)
            .where(inquiryProduct.inquiry.idx.eq(inquiryIdx)
                .and(inquiryProduct.product.idx.eq(productIdx)))
            .fetchFirst();
    }

    public void saveGiftboxInquiryResult(GiftboxInquiryResult giftboxInquiryResult) {
        em.persist(giftboxInquiryResult);
    }

    public List<Product> findProductListByGiftbox(Long giftboxIdx) {
        return queryFactory
            .select(giftboxInquiryResult.product)
            .from(giftboxInquiryResult)
            .where(giftboxInquiryResult.giftbox.idx.eq(giftboxIdx))
            .fetch();
    }

    public Long findEmojiByGiftboxProduct(Long giftboxIdx, Long idx) {
        return queryFactory
            .select(giftboxInquiryResult.emoji.idx)
            .from(giftboxInquiryResult)
            .where(giftboxInquiryResult.giftbox.idx.eq(giftboxIdx)
                .and(giftboxInquiryResult.product.idx.eq(idx)))
            .fetchFirst();
    }

    public boolean existGiftboxAndProduct(Giftbox giftbox, Long productIdx) {
        return queryFactory
            .select(giftboxInquiryResult)
            .from(giftboxInquiryResult)
            .where(giftboxInquiryResult.giftbox.eq(giftbox)
                .and(giftboxInquiryResult.product.idx.eq(productIdx)))
            .fetchFirst() != null;
    }

}
