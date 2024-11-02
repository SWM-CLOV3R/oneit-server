package clov3r.api.friend.repository;

import clov3r.api.friend.domain.entity.Friendship;
import java.util.List;

public interface FriendshipCustomRepository {
  public Friendship findByUserIdxAndFriendIdx(Long userIdx, Long friendIdx);
  public List<Friendship> findByUserIdx(Long userIdx);
}
