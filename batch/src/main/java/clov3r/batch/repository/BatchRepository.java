package clov3r.batch.repository;

import static clov3r.domain.domains.entity.QFriendship.friendship;
import static clov3r.domain.domains.entity.QUser.user;

import clov3r.domain.domains.entity.Friendship;
import clov3r.domain.domains.entity.Notification;
import clov3r.domain.domains.entity.User;
import clov3r.domain.domains.status.Status;
import clov3r.domain.domains.status.UserStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BatchRepository {
  private final EntityManager entityManager;
  private final JPAQueryFactory queryFactory;

  public List<User> findUsersWithBirthdayInDays(int days) {
    LocalDate today = LocalDate.now();
    LocalDate targetDate = today.plusDays(days);
    return queryFactory.select(user)
        .from(user)
        .where(
            (
                user.birthDate.month().eq(today.getMonthValue())
                    .and(user.birthDate.dayOfMonth().eq(today.plusDays(days).getDayOfMonth()))
            )
                .or(
                    user.birthDate.month().eq(targetDate.getMonthValue())
                        .and(user.birthDate.dayOfMonth().eq(targetDate.getDayOfMonth()))
                )
        )
        .where(user.status.eq(UserStatus.ACTIVE))
        .fetch();
  }

  public List<Friendship> findByUserIdx(Long userIdx) {
    return queryFactory.select(friendship)
        .from(friendship)
        .where(friendship.user.idx.eq(userIdx))
        .where(friendship.status.eq(Status.ACTIVE)
            .and(friendship.friend.status.eq(UserStatus.ACTIVE)))
        .fetch();
  }

  public void saveNotification(Notification notification) {
    entityManager.persist(notification);
  }

//  public boolean existsProductLike(Long userIdx) {
//    return queryFactory.select(productLike)
//        .from(productLike)
//        .where(productLike.user.idx.eq(userIdx)
//            .and(productLike.likeStatus.eq(LikeStatus.LIKE)))
//        .fetchFirst() != null;
//  }

}
