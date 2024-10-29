package clov3r.api.giftbox.repository;

import static clov3r.api.giftbox.domain.entity.QGiftboxProduct.giftboxProduct;
import static clov3r.api.giftbox.domain.entity.QGiftboxProductVote.giftboxProductVote;

import clov3r.api.giftbox.domain.status.VoteStatus;
import clov3r.api.giftbox.domain.entity.GiftboxProductVote;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GiftboxProductCustomVoteRepositoryImpl implements GiftboxProductCustomVoteRepository {

  @PersistenceContext
  private final EntityManager em;
  private final JPAQueryFactory queryFactory;

  public VoteStatus voteProduct(GiftboxProductVote newVote) {
    try {
      GiftboxProductVote result = queryFactory.select(giftboxProductVote)
          .from(giftboxProductVote)
          .where(giftboxProductVote.id.giftboxIdx.eq(newVote.getId().getGiftboxIdx())
              .and(giftboxProductVote.id.productIdx.eq(newVote.getId().getProductIdx()))
              .and(giftboxProductVote.id.userIdx.eq(newVote.getId().getUserIdx())))
          .fetchOne();
      VoteStatus previousVote = result == null ? null : result.getVote();
      if (result == null) {
        em.persist(newVote);
      }
      else {
        em.merge(newVote);
      }
      return previousVote;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }


  @Override
  public VoteStatus getVoteStatusOfUser(Long userIdx, Long giftboxIdx, Long productIdx) {
    return queryFactory.select(giftboxProductVote.vote)
        .from(giftboxProductVote)
        .where(giftboxProductVote.id.userIdx.eq(userIdx)
            .and(giftboxProductVote.id.giftboxIdx.eq(giftboxIdx))
            .and(giftboxProductVote.id.productIdx.eq(productIdx)))
        .fetchOne();
  }

  @Override
  public void updateLikeCount(Long giftboxIdx, Long productIdx, int i) {
    queryFactory.update(giftboxProduct)
        .where(giftboxProduct.giftbox.idx.eq(giftboxIdx)
            .and(giftboxProduct.product.idx.eq(productIdx)))
        .set(giftboxProduct.likeCount, giftboxProduct.likeCount.add(i))
        .execute();
  }

  @Override
  public void updateDislikeCount(Long giftboxIdx, Long productIdx, int i) {
    queryFactory.update(giftboxProduct)
        .where(giftboxProduct.giftbox.idx.eq(giftboxIdx)
            .and(giftboxProduct.product.idx.eq(productIdx)))
        .set(giftboxProduct.dislikeCount, giftboxProduct.dislikeCount.add(i))
        .execute();
  }

}