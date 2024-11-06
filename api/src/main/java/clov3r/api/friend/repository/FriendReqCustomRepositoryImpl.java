package clov3r.api.friend.repository;

import static clov3r.domain.domains.entity.QFriendReq.friendReq;

import clov3r.domain.domains.entity.FriendReq;
import clov3r.domain.domains.status.FriendReqStatus;
import clov3r.domain.domains.status.UserStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FriendReqCustomRepositoryImpl implements FriendReqCustomRepository {
  private final JPAQueryFactory queryFactory;

  @Override
  public FriendReq findByIdx(Long requestIdx) {
    return queryFactory.select(friendReq)
        .from(friendReq)
        .where(friendReq.idx.eq(requestIdx))
        .where(friendReq.friendReqStatus.eq(FriendReqStatus.REQUESTED)
            .and(friendReq.to.status.eq(UserStatus.ACTIVE))
            .and(friendReq.from.status.eq(UserStatus.ACTIVE)))
        .fetchOne();
  }

  @Override
  public FriendReq findByFromIdxAndToIdx(Long fromIdx, Long toIdx) {
    return queryFactory.select(friendReq)
        .from(friendReq)
        .where(friendReq.from.idx.eq(fromIdx))
        .where(friendReq.to.idx.eq(toIdx))
        .where(friendReq.friendReqStatus.eq(FriendReqStatus.REQUESTED)
            .and(friendReq.to.status.eq(UserStatus.ACTIVE))
            .and(friendReq.from.status.eq(UserStatus.ACTIVE)))
        .fetchOne();
  }

  @Override
  public List<FriendReq> findAllByToIdx(Long userIdx) {
    return queryFactory.select(friendReq)
        .from(friendReq)
        .where(friendReq.to.idx.eq(userIdx))
        .where(friendReq.friendReqStatus.eq(FriendReqStatus.REQUESTED)
            .and(friendReq.to.status.eq(UserStatus.ACTIVE))
            .and(friendReq.from.status.eq(UserStatus.ACTIVE)))
        .fetch();
  }

  @Override
  public List<FriendReq> findAllByFromIdx(Long userIdx) {
    return queryFactory.select(friendReq)
        .from(friendReq)
        .where(friendReq.from.idx.eq(userIdx))
        .where(friendReq.friendReqStatus.eq(FriendReqStatus.REQUESTED)
            .and(friendReq.to.status.eq(UserStatus.ACTIVE))
            .and(friendReq.from.status.eq(UserStatus.ACTIVE)))
        .fetch();
  }
}
