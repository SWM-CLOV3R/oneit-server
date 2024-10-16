package clov3r.oneit_server.service;

import clov3r.oneit_server.domain.data.status.VoteStatus;
import clov3r.oneit_server.domain.entity.GiftboxProductVote;
import clov3r.oneit_server.repository.GiftboxProductVoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GiftboxProductService {

  private final GiftboxProductVoteRepository giftboxProductVoteRepository;

  public VoteStatus voteProduct(GiftboxProductVote giftboxProductVote) {
      VoteStatus previousStatus = giftboxProductVoteRepository.voteProduct(giftboxProductVote);
      return previousStatus;
  }

  public void updateVoteCount(Long giftboxIdx, Long productIdx, VoteStatus previousVote, VoteStatus newVote) {

      // 이전 상태가 없는 경우
      if (previousVote == null) {

        if (newVote.equals(VoteStatus.LIKE)) {
          // like +1
          giftboxProductVoteRepository.updateLikeCount(giftboxIdx, productIdx, 1);
        } else if (newVote.equals(VoteStatus.DISLIKE)) {
          // dislike +1
          giftboxProductVoteRepository.updateDislikeCount(giftboxIdx, productIdx, 1);
        }
      } else {

        // 상태가 바뀔 경우에만 업데이트
        // 이전 상태가 LIKE or DISLIKE 인 경우 -> LIKE  -1 / DISLIKE -1
        // 새로운 상태가 LIKE or DISLIKE 인 경우 -> LIKE +1 / DISLIKE +1
        if (!previousVote.equals(newVote)) {
          if (previousVote.equals(VoteStatus.LIKE)) {
            // like -1
            giftboxProductVoteRepository.updateLikeCount(giftboxIdx, productIdx, -1);
          } else if (previousVote.equals(VoteStatus.DISLIKE)) {
            // dislike -1
            giftboxProductVoteRepository.updateDislikeCount(giftboxIdx, productIdx, -1);
          }

          if (newVote.equals(VoteStatus.LIKE)) {
            // like +1
            giftboxProductVoteRepository.updateLikeCount(giftboxIdx, productIdx, 1);
          } else if (newVote.equals(VoteStatus.DISLIKE)) {
            // dislike +1
            giftboxProductVoteRepository.updateDislikeCount(giftboxIdx, productIdx, 1);
          }
        }

      }

  }


  public VoteStatus getVoteStatusOfUser(Long userIdx, Long giftboxIdx, Long idx) {
    VoteStatus voteStatus = giftboxProductVoteRepository.getVoteStatusOfUser(userIdx, giftboxIdx, idx);
    if (voteStatus == null || voteStatus == VoteStatus.NONE) {
      return VoteStatus.NONE;
    }
    return voteStatus;
  }

}
