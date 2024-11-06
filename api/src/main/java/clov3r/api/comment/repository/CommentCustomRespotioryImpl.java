package clov3r.api.comment.repository;

import static clov3r.domain.domains.entity.QComment.comment;

import clov3r.domain.domains.entity.Comment;
import clov3r.domain.domains.status.Status;
import clov3r.domain.domains.status.UserStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentCustomRespotioryImpl implements CommentCustomRepository {
  private final JPAQueryFactory queryFactory;

  @Override
  public List<Comment> findActiveByGiftboxProductIdx(Long giftboxProductIdx) {
    return queryFactory
        .selectFrom(comment)
        .where(comment.giftboxProductIdx.eq(giftboxProductIdx))
        .where(comment.writer.status.eq(UserStatus.ACTIVE)
            .and(comment.status.eq(Status.ACTIVE)))
        .fetch();
  }
}
