package clov3r.api.friend.repository;

import clov3r.api.friend.domain.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository
    extends JpaRepository<Friendship, Long>, FriendshipCustomRepository {

}
