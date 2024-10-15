package clov3r.api.repository;

import clov3r.api.domain.data.status.VoteStatus;
import clov3r.api.domain.entity.GiftboxProductVote;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftboxProductCustomVoteRepository {

  VoteStatus voteProduct(GiftboxProductVote giftboxProductVote);

  VoteStatus getVoteStatusOfUser(Long userIdx, Long giftboxIdx, Long idx);

  void updateLikeCount(Long giftboxIdx, Long productIdx, int i);

  void updateDislikeCount(Long giftboxIdx, Long productIdx, int i);
}
