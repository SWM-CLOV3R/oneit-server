package clov3r.oneit_server.repository;

import clov3r.oneit_server.domain.data.status.VoteStatus;
import clov3r.oneit_server.domain.entity.GiftboxProductVote;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftboxProductCustomRepository {

  VoteStatus voteProduct(GiftboxProductVote giftboxProductVote);

  VoteStatus getVoteStatusOfUser(Long userIdx, Long giftboxIdx, Long idx);

  void updateLikeCount(Long giftboxIdx, Long productIdx, int i);

  void updateDislikeCount(Long giftboxIdx, Long productIdx, int i);
}
