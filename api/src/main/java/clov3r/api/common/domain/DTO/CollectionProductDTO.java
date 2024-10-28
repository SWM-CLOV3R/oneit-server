package clov3r.api.common.domain.DTO;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionProductDTO {
  private Long collectionIdx;
  private String collectionName;
  private String collectionDescription;
  private String collectionThumbnailUrl;
  private List<ProductDTO> productList;
}
