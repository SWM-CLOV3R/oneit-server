package clov3r.api.product.domain.entity;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import clov3r.api.common.domain.entity.BaseEntity;
import clov3r.api.product.domain.status.LikeStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "product_like")
public class ProductLike extends BaseEntity {
  @Id @GeneratedValue(strategy = IDENTITY)
  private Long idx;
  @Column(name = "user_idx")
  private Long userIdx;
  @Column(name = "product_idx")
  private Long productIdx;

  @Setter
  @Column(name = "like_status")
  @Enumerated(STRING)
  private LikeStatus likeStatus;

}
