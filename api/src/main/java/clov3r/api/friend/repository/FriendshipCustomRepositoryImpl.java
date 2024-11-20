package clov3r.api.friend.repository;

import static clov3r.domain.domains.entity.QFriendship.friendship;

import clov3r.domain.domains.entity.Friendship;
import clov3r.domain.domains.entity.User;
import clov3r.domain.domains.status.Status;
import clov3r.domain.domains.status.UserStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.MonthDay;
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

  @Override
  public List<User> findBirthdayFriends(Long userIdx, int days) {
    LocalDate now = LocalDate.now();
    MonthDay today = MonthDay.from(now);
    MonthDay last = MonthDay.from(now.plusDays(days));
    boolean isChangeMonth =
        (today.getMonth().getValue() == 12 && last.getMonth().getValue() == 1)
        || (today.getMonth().getValue() < last.getMonth().getValue());

    return queryFactory.select(friendship.friend)
        .from(friendship)
        .where(friendship.user.idx.eq(userIdx))
        .where(friendship.status.eq(Status.ACTIVE)
            .and(friendship.friend.status.eq(UserStatus.ACTIVE)))
        .where(isChangeMonth ?
            (friendship.friend.birthDate.month().eq(last.getMonthValue())
            .and(friendship.friend.birthDate.dayOfMonth().between(1, last.getDayOfMonth())))
            .or(friendship.friend.birthDate.month().eq(today.getMonthValue())
                .and(friendship.friend.birthDate.dayOfMonth().between(today.getDayOfMonth(), today.getDayOfMonth()+days)))
            :
                friendship.friend.birthDate.month().eq(today.getMonthValue())
            .and(friendship.friend.birthDate.dayOfMonth().between(today.getDayOfMonth(), today.getDayOfMonth()+days))
        )
        .fetch();
  }


}
