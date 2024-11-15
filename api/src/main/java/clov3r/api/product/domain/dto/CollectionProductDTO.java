package clov3r.api.product.domain.dto;

import clov3r.api.product.domain.status.ProductStatus;
import clov3r.domain.domains.entity.Keyword;
import clov3r.domain.domains.entity.Product;
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
  private List<String> displayTags;
  private String showcaseImageUrl;
  private ProductStatus productStatus;

  public CollectionProductDTO(Product product, String showcaseImageUrl) {
    this.productIdx = product.getIdx();
    this.productName = product.getName();
    this.keywords = product.getKeyword();
    this.displayTags = product.getDisplayTags();
    this.showcaseImageUrl = showcaseImageUrl;
    this.productStatus = product.getStatus();
  }
}
