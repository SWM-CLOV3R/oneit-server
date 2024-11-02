package clov3r.api.comment.repository;

import clov3r.api.comment.domain.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {

    @Query("select c from Comment c where c.idx = :idx and c.deletedAt is null")
    Comment findByIdx(Long idx);

}
