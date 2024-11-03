package clov3r.api.notification.repository;

import static clov3r.api.notification.domain.entity.QNotification.notification;

import clov3r.api.notification.domain.entity.Notification;
import clov3r.api.notification.domain.status.NotiStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationCustomRepositoryImpl implements NotificationCustomRepository {

  private final JPAQueryFactory queryFactory;

  public List<Notification> findAllByUserId(Long userIdx) {
    return queryFactory.select(notification)
        .from(notification)
        .where(notification.receiver.idx.eq(userIdx))
        .where(notification.notiStatus.ne(NotiStatus.DELETED))
        .orderBy(notification.createdAt.desc())
        .fetch();
  }

  @Override
  public Notification fincByIdxAndUserIdx(Long idx, Long userIdx) {
    return queryFactory.selectFrom(notification)
        .where(notification.idx.eq(idx)
            .and(notification.receiver.idx.eq(userIdx)))
        .fetchOne();
  }
}
