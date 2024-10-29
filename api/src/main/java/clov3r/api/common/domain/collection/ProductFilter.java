package clov3r.api.common.domain.collection;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductFilter {
  private int minPrice;
  private int maxPrice;
}
