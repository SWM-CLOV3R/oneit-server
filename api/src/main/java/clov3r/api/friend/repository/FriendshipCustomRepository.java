package clov3r.api.friend.repository;

import clov3r.domain.domains.entity.Friendship;
import clov3r.domain.domains.entity.User;
import java.util.List;

public interface FriendshipCustomRepository {
  Friendship findByUserIdxAndFriendIdx(Long userIdx, Long friendIdx);
  List<Friendship> findByUserIdx(Long userIdx);

  List<User> findBirthdayFriends(Long userIdx, int days);
}
