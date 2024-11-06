package clov3r.api.comment.service;

import clov3r.api.comment.domain.dto.CommentDTO;
import clov3r.api.auth.domain.request.CommentRequest;
import clov3r.api.comment.repository.CommentRepository;
import clov3r.api.auth.repository.UserRepository;
import clov3r.domain.domains.entity.Comment;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
        request.getContent());
    commentRepository.save(comment);
    return comment;
  }

  public List<CommentDTO> getCommentList(Long giftboxProductIdx) {
    List<Comment> comments = commentRepository.findActiveByGiftboxProductIdx(giftboxProductIdx);
    return CommentDTO.listOf(comments);
  }

  @Transactional
  public void deleteComment(Long commentIdx) {
    Comment comment = commentRepository.findByIdx(commentIdx);
    comment.deleteComment();
  }
}
