package clov3r.api.giftbox.repository;

import clov3r.api.giftbox.domain.status.VoteStatus;
import clov3r.api.giftbox.domain.entity.GiftboxProductVote;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftboxProductCustomVoteRepository {

  VoteStatus voteProduct(GiftboxProductVote giftboxProductVote);

  VoteStatus getVoteStatusOfUser(Long userIdx, Long giftboxIdx, Long idx);

  void updateLikeCount(Long giftboxIdx, Long productIdx, int i);

  void updateDislikeCount(Long giftboxIdx, Long productIdx, int i);
}
