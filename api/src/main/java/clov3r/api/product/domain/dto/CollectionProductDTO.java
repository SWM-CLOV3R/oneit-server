package clov3r.api.product.domain.dto;

import clov3r.api.product.domain.status.ProductStatus;
import clov3r.domain.domains.entity.Keyword;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CollectionProductDTO {
  private Long productIdx;
  private String productName;
  private List<Keyword> keywords;
  private String showcaseImageUrl;
  private ProductStatus productStatus;
}
