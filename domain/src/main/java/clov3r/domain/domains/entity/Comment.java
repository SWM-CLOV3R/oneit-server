package clov3r.domain.domains.entity;

import clov3r.domain.domains.status.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "writer_idx")
  private User writer;
  private String content;

  @Enumerated(value = EnumType.STRING)
  private Status status;

  public Comment(Long giftboxProductIdx, User byUser, String content) {
    this.giftboxProductIdx = giftboxProductIdx;
    this.writer = byUser;
    this.content = content;
    this.status = Status.ACTIVE;
    this.createBaseEntity();
  }

  public void deleteComment() {
    this.status = Status.DELETED;
    this.deleteBaseEntity();
  }
}
