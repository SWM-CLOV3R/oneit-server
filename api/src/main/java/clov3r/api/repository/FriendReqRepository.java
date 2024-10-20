package clov3r.api.repository;

import clov3r.api.domain.entity.FriendReq;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendReqRepository extends JpaRepository<FriendReq, Long> {

  @Query("select f from FriendReq f where f.idx = :requestIdx and f.friendReqStatus = 'REQUESTED'")
  FriendReq findByIdx(Long requestIdx);

  @Query("select f from FriendReq f where f.from.idx = :fromIdx and f.to.idx = :toIdx and f.friendReqStatus = 'REQUESTED'")
  FriendReq findByFromIdxAndToIdx(Long fromIdx, Long toIdx);

  @Query("select f from FriendReq f where f.to.idx = :userIdx and f.friendReqStatus = 'REQUESTED'")
  List<FriendReq> findAllByToIdx(Long userIdx);

  @Query("select f from FriendReq f where f.from.idx = :userIdx and f.friendReqStatus = 'REQUESTED'")
  List<FriendReq> findAllByFromIdx(Long userIdx);

}
