package clov3r.api.friend.repository;

import clov3r.domain.domains.entity.FriendReq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendReqRepository
    extends JpaRepository<FriendReq, Long>, FriendReqCustomRepository {
}
