package clov3r.api.comment.repository;

import clov3r.api.comment.domain.entity.Comment;
import java.util.List;

public interface CommentCustomRepository {
  List<Comment> findAllByGiftboxProductIdx(Long giftboxProductIdx);

}
