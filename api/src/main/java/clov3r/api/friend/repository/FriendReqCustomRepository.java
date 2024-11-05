package clov3r.api.friend.repository;

import clov3r.domain.domains.entity.FriendReq;
import java.util.List;

public interface FriendReqCustomRepository {
  FriendReq findByIdx(Long requestIdx);
  FriendReq findByFromIdxAndToIdx(Long fromIdx, Long toIdx);
  List<FriendReq> findAllByToIdx(Long userIdx);
  List<FriendReq> findAllByFromIdx(Long userIdx);
}
