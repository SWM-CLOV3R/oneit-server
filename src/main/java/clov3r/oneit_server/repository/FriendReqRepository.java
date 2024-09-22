package clov3r.oneit_server.repository;

import clov3r.oneit_server.domain.entity.FriendReq;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendReqRepository extends JpaRepository<FriendReq, Long> {

  @Query("select f from FriendReq f where f.from.idx = :userIdx and f.to.idx = :friendIdx")
  FriendReq findByFromIdxAndToIdx(Long userIdx, Long friendIdx);

  @Query("select f from FriendReq f where f.to.idx = :userIdx and f.friendReqStatus = 'REQUESTED'")
  List<FriendReq> findAllByToIdx(Long userIdx);
}
