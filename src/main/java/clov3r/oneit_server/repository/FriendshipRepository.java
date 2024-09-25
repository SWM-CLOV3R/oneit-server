package clov3r.oneit_server.repository;

import clov3r.oneit_server.domain.entity.Friendship;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

  @Query("select f from Friendship f where f.user.idx = :userIdx and f.friend.idx = :friendIdx and f.status = 'ACTIVE'")
  Friendship findByUserIdxAndFriendIdx(Long userIdx, Long friendIdx);

  @Query("select f from Friendship f where f.user.idx = :userIdx and f.status = 'ACTIVE'")
  List<Friendship> findByUserIdx(Long userIdx);

}
