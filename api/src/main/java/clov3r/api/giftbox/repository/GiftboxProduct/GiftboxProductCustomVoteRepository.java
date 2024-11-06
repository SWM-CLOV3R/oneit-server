package clov3r.api.giftbox.repository.GiftboxProduct;

import clov3r.domain.domains.entity.GiftboxProductVote;
import clov3r.domain.domains.status.VoteStatus;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftboxProductCustomVoteRepository {

  VoteStatus voteProduct(GiftboxProductVote giftboxProductVote);

  VoteStatus getVoteStatusOfUser(Long userIdx, Long giftboxIdx, Long idx);

  void updateLikeCount(Long giftboxIdx, Long productIdx, int i);

  void updateDislikeCount(Long giftboxIdx, Long productIdx, int i);
}
