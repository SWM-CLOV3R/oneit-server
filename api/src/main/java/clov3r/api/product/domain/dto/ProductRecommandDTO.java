package clov3r.api.product.domain.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRecommandDTO {
  List<ProductDTO> products;
  List<ProductDTO> relatedProducts;

  public ProductRecommandDTO(List<ProductDTO> products, List<ProductDTO> relatedProducts) {
    this.products = products;
    this.relatedProducts = relatedProducts;
  }

}
