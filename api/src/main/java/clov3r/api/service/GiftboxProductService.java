package clov3r.api.service;

import clov3r.api.domain.data.status.VoteStatus;
import clov3r.api.domain.entity.GiftboxProductVote;
import clov3r.api.repository.GiftboxProductRepository;
import clov3r.api.repository.GiftboxProductVoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class GiftboxProductService {

  private final GiftboxProductVoteRepository giftboxProductVoteRepository;
  private final GiftboxProductRepository giftboxProductRepository;

  public VoteStatus voteProduct(GiftboxProductVote giftboxProductVote) {
    return giftboxProductVoteRepository.voteProduct(giftboxProductVote);
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


  public VoteStatus getVoteStatusOfUser(Long userIdx, Long giftboxIdx, Long productIdx) {
    VoteStatus voteStatus = giftboxProductVoteRepository.getVoteStatusOfUser(userIdx, giftboxIdx, productIdx);
    if (voteStatus == null || voteStatus == VoteStatus.NONE) {
      return VoteStatus.NONE;
    }
    return voteStatus;
  }

  public void purchaseProduct(Long giftboxIdx, Long productIdx) {
    giftboxProductRepository.purchaseProduct(giftboxIdx, productIdx);
  }
}
