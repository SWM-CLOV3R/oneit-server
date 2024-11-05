package clov3r.domain.domains.entity;

import clov3r.api.product.domain.status.LikeStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "product_like")
public class ProductLike extends BaseEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idx;
  @Column(name = "user_idx")
  private Long userIdx;
  @Column(name = "product_idx")
  private Long productIdx;

  @Setter
  @Column(name = "like_status")
  @Enumerated(EnumType.STRING)
  private LikeStatus likeStatus;

}
