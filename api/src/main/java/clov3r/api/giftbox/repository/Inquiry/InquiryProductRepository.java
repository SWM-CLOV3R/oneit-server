package clov3r.api.giftbox.repository.Inquiry;

import static clov3r.domain.domains.entity.QInquiryProduct.inquiryProduct;

import clov3r.api.giftbox.domain.request.ProductEmoji;
import clov3r.api.product.domain.status.ProductStatus;
import clov3r.domain.domains.entity.Giftbox;
import clov3r.domain.domains.entity.Inquiry;
import clov3r.domain.domains.entity.InquiryProduct;
import clov3r.domain.domains.entity.Product;
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
            .where(inquiryProduct.product.status.eq(ProductStatus.ACTIVE)
                .or(inquiryProduct.product.status.ne(ProductStatus.INVALID)))
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

}
