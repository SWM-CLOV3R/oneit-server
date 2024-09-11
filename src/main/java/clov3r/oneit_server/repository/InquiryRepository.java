package clov3r.oneit_server.repository;

import static clov3r.oneit_server.domain.entity.QInquiryProduct.inquiryProduct;

import clov3r.oneit_server.domain.entity.Inquiry;
import clov3r.oneit_server.domain.entity.InquiryProduct;
import clov3r.oneit_server.domain.entity.Product;
import clov3r.oneit_server.domain.request.InquiryRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

  @Transactional
  public void addProductToInquiry(Inquiry inquiry, Product product) {
    InquiryProduct inquiryProduct = new InquiryProduct(
        inquiry,
        product
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
}
