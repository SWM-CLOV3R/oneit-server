package clov3r.api.comment.repository;

import clov3r.api.comment.domain.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface CommentCustomRepository {
  @Query(
      "select c from Comment c "
          + "where c.giftboxProductIdx = :giftboxProductIdx "
          + "and c.writer.status = 'ACTIVE' "
          + "and c.status = 'ACTIVE'"
  )
  List<Comment> findActiveByGiftboxProductIdx(Long giftboxProductIdx);
}
