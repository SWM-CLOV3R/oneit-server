package clov3r.api.comment.repository;

import clov3r.domain.domains.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface CommentCustomRepository {
  @Query(
      "select c from Comment c "
          + "where c.giftboxProductIdx = :giftboxProductIdx "
          + "and c.status = 'ACTIVE'"
  )
  List<Comment> findActiveByGiftboxProductIdx(Long giftboxProductIdx);
}
