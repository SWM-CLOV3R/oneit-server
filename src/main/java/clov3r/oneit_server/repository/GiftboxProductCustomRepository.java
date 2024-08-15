package clov3r.oneit_server.repository;

import clov3r.oneit_server.domain.data.status.VoteStatus;
import clov3r.oneit_server.domain.entity.GiftboxProductVote;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftboxProductCustomRepository {

  void voteProduct(GiftboxProductVote giftboxProductVote);

  void countLike(Long giftboxIdx, Long productIdx);

  VoteStatus getVoteStatusOfUser(Long userIdx, Long giftboxIdx, Long idx);

}
