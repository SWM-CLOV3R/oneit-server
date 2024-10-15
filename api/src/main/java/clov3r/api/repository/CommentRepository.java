package clov3r.api.repository;

import clov3r.api.domain.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.giftboxProductIdx = :giftboxProductIdx and c.deletedAt is null")
    List<Comment> findAllByGiftboxProductIdx(Long giftboxProductIdx);

    @Query("select c from Comment c where c.idx = :idx and c.deletedAt is null")
    Comment findByIdx(Long idx);

}
