package clov3r.api.product.domain.entity;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import clov3r.api.common.domain.entity.BaseEntity;
import clov3r.api.common.domain.status.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "collection_product")
public class CollectionProduct extends BaseEntity {

  @Id @GeneratedValue(strategy = IDENTITY)
  private Long idx;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "collection_idx")
  private Collection collection;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "product_idx")
  private Product product;

  @Column(name = "detail_img")
  private String detailImg;

  @Enumerated(EnumType.STRING)
  private Status status;

}
