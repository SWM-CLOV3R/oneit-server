package clov3r.api.domain.entity;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import clov3r.api.domain.data.status.LikeStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Builder
@Getter
@Entity
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
