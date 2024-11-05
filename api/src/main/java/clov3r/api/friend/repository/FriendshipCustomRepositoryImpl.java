package clov3r.api.friend.repository;

import static clov3r.domain.domains.entity.QFriendship.friendship;

import clov3r.domain.domains.entity.Friendship;
import clov3r.domain.domains.status.Status;
import clov3r.domain.domains.status.UserStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FriendshipCustomRepositoryImpl implements FriendshipCustomRepository {
  private final JPAQueryFactory queryFactory;

  public Friendship findByUserIdxAndFriendIdx(Long userIdx, Long friendIdx) {
    return queryFactory.select(friendship)
        .from(friendship)
        .where(friendship.user.idx.eq(userIdx)
            .and(friendship.friend.idx.eq(friendIdx)))
        .where(friendship.status.eq(Status.ACTIVE)
            .and(friendship.friend.status.eq(UserStatus.ACTIVE)))
        .fetchOne();
  }

  public List<Friendship> findByUserIdx(Long userIdx) {
    return queryFactory.select(friendship)
        .from(friendship)
        .where(friendship.user.idx.eq(userIdx))
        .where(friendship.status.eq(Status.ACTIVE)
            .and(friendship.friend.status.eq(UserStatus.ACTIVE)))
        .fetch();
  }


}
