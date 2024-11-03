package clov3r.api.comment.service;

import clov3r.api.comment.domain.dto.CommentDTO;
import clov3r.api.comment.domain.entity.Comment;
import clov3r.api.auth.domain.request.CommentRequest;
import clov3r.api.comment.repository.CommentRepository;
import clov3r.api.auth.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final UserRepository userRepository;

  public Comment createComment(Long writerIdx, Long giftboxProductIdx, CommentRequest request) {

    Comment comment = new Comment(
        giftboxProductIdx,
        userRepository.findByUserIdx(writerIdx),
        request);
    commentRepository.save(comment);
    return comment;
  }

  public List<CommentDTO> getCommentList(Long giftboxProductIdx) {
    List<Comment> comments = commentRepository.findAllByGiftboxProductIdx(giftboxProductIdx);
    return CommentDTO.listOf(comments);
  }

  @Transactional
  public void deleteComment(Long commentIdx) {
    Comment comment = commentRepository.findByIdx(commentIdx);
    comment.deleteComment();
  }
}
