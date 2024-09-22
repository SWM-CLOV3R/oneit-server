package clov3r.oneit_server.repository;

import clov3r.oneit_server.domain.entity.FriendReq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendReqRepository extends JpaRepository<FriendReq, Long> {

  @Query("select f from FriendReq f where f.from.idx = ?1 and f.to.idx = ?2")
  FriendReq findByFromIdxAndToIdx(Long userIdx, Long friendIdx);
}
