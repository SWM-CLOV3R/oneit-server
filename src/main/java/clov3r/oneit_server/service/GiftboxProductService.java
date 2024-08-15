package clov3r.oneit_server.service;

import clov3r.oneit_server.domain.data.status.VoteStatus;
import clov3r.oneit_server.domain.entity.GiftboxProductVote;
import clov3r.oneit_server.exception.BaseException;
import clov3r.oneit_server.repository.GiftboxProductRepository;
import clov3r.oneit_server.response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GiftboxProductService {
    private final GiftboxProductRepository giftboxProductRepository;

    @Transactional
    public void voteProduct(GiftboxProductVote giftboxProductVote) {
      try {
        giftboxProductRepository.voteProduct(giftboxProductVote);

      } catch (BaseException e) {
        System.out.println("GiftboxProductService.voteProduct");
        System.out.println("e = " + e);
        throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
      }
    }

    @Transactional
    public void countLike(Long giftboxIdx, Long productIdx) {
      giftboxProductRepository.countLike(giftboxIdx, productIdx);
    }


    public VoteStatus getVoteStatusOfUser(Long userIdx, Long giftboxIdx, Long idx) {
      VoteStatus voteStatus = giftboxProductRepository.getVoteStatusOfUser(userIdx, giftboxIdx, idx);
      if (voteStatus == null) {
        return VoteStatus.NONE;
      }
      return voteStatus;
    }

}
