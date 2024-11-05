package clov3r.api.friend.repository;

import clov3r.domain.domains.entity.Friendship;
import java.util.List;

public interface FriendshipCustomRepository {
  Friendship findByUserIdxAndFriendIdx(Long userIdx, Long friendIdx);
  List<Friendship> findByUserIdx(Long userIdx);
}
