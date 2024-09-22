package clov3r.oneit_server.repository;

import clov3r.oneit_server.domain.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
  Friendship findByUserIdxAndFriendIdx(Long userIdx, Long friendIdx);

}
