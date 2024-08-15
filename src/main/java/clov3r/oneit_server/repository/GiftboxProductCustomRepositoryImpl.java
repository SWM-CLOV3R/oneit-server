package clov3r.oneit_server.repository;

import static clov3r.oneit_server.domain.entity.QGiftboxProduct.giftboxProduct;
import static clov3r.oneit_server.domain.entity.QGiftboxProductVote.giftboxProductVote;

import clov3r.oneit_server.domain.data.status.VoteStatus;
import clov3r.oneit_server.domain.entity.GiftboxProductVote;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class GiftboxProductCustomRepositoryImpl implements GiftboxProductCustomRepository {

  @PersistenceContext
  private final EntityManager em;
  private final JPAQueryFactory queryFactory;

  public void voteProduct(GiftboxProductVote giftboxProductVote) {
    try {
      em.persist(giftboxProductVote);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  public void countLike(Long giftboxIdx, Long productIdx) {
    try {
      queryFactory.update(giftboxProduct)
          .where(giftboxProduct.giftbox.idx.eq(giftboxIdx)
              .and(giftboxProduct.product.idx.eq(productIdx)))
          .set(giftboxProduct.likeCount, giftboxProduct.likeCount.add(1))
          .execute();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

  }

  @Override
  public VoteStatus getVoteStatusOfUser(Long userIdx, Long giftboxIdx, Long productIdx) {
    return queryFactory.select(giftboxProductVote.vote)
        .from(giftboxProductVote)
        .where(giftboxProductVote.user.idx.eq(userIdx)
            .and(giftboxProductVote.id.giftboxIdx.eq(giftboxIdx))
            .and(giftboxProductVote.id.productIdx.eq(productIdx)))
        .fetchOne();
  }

}
