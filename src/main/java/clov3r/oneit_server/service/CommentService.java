package clov3r.oneit_server.service;

import clov3r.oneit_server.domain.DTO.CommentDTO;
import clov3r.oneit_server.domain.entity.Comment;
import clov3r.oneit_server.domain.request.CommentRequest;
import clov3r.oneit_server.repository.CommentRepository;
import clov3r.oneit_server.repository.UserRepository;
import java.time.LocalDateTime;
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
    Comment comment = Comment.builder()
        .giftboxProductIdx(giftboxProductIdx)
        .writer(userRepository.findByUserIdx(writerIdx))
        .content(request.getContent())
        .build();
    comment.createBaseEntity();
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
    comment.setDeletedAt(LocalDateTime.now());
  }
}
