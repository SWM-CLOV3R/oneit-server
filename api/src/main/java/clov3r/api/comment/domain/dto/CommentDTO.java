package clov3r.api.comment.domain.dto;

import clov3r.api.comment.domain.entity.Comment;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

  private Long idx;
  private Long giftboxProductIdx;
  private Long writerIdx;
  private String writerNickName;
  private String writerProfileImg;
  private String content;
  private LocalDateTime createdAt;

  public CommentDTO (Comment comment) {
    this.idx = comment.getIdx();
    this.giftboxProductIdx = comment.getGiftboxProductIdx();
    this.writerIdx = comment.getWriter().getIdx();
    this.writerNickName = comment.getWriter().getNickname();
    this.writerProfileImg = comment.getWriter().getProfileImg();
    this.content = comment.getContent();
    this.createdAt = comment.getCreatedAt();
  }

  public static List<CommentDTO> listOf(List<Comment> comments) {
    return comments.stream()
        .map(CommentDTO::new)
        .toList();
  }

}
