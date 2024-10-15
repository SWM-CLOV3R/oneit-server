package clov3r.api.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseEntity {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idx;

  @Column(name = "giftbox_product_idx")
  private Long giftboxProductIdx;

  @ManyToOne
  @JoinColumn(name = "writer_idx")
  private User writer;
  private String content;

}
