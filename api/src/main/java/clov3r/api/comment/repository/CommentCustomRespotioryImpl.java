package clov3r.api.comment.repository;

import static clov3r.api.comment.domain.entity.QComment.comment;

import clov3r.api.auth.domain.data.UserStatus;
import clov3r.api.comment.domain.entity.Comment;
import clov3r.api.common.domain.status.Status;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentCustomRespotioryImpl implements CommentCustomRepository {
  private final JPAQueryFactory queryFactory;

  @Override
  public List<Comment> findAllByGiftboxProductIdx(Long giftboxProductIdx) {
    return queryFactory
        .selectFrom(comment)
        .where(comment.giftboxProductIdx.eq(giftboxProductIdx))
        .where(comment.writer.status.eq(UserStatus.ACTIVE)
            .and(comment.deletedAt.isNotNull()))
        .fetch();
  }
}
