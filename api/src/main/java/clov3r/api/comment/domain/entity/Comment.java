package clov3r.api.comment.domain.entity;

import clov3r.api.auth.domain.request.CommentRequest;
import clov3r.api.common.domain.entity.BaseEntity;
import clov3r.api.auth.domain.entity.User;
import clov3r.api.common.domain.status.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseEntity {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idx;

  @Column(name = "giftbox_product_idx")
  private Long giftboxProductIdx;

  @ManyToOne
  @JoinColumn(name = "writer_idx")
  private User writer;
  private String content;

  public Comment(Long giftboxProductIdx, User byUser, CommentRequest request) {
    this.giftboxProductIdx = giftboxProductIdx;
    this.writer = byUser;
    this.content = request.getContent();
    this.createBaseEntity();
  }

  public void deleteComment() {
    this.deleteBaseEntity();
  }
}
