package clov3r.api.product.domain.dto;

import clov3r.api.product.domain.entity.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CollectionDetailDTO {
  private Long collectionIdx;
  private String collectionName;
  private String collectionDescription;
  private String collectionThumbnailUrl;
  private List<CollectionProductDTO> collectionProductDTOList;

  public CollectionDetailDTO(Collection collection, List<CollectionProductDTO> collectionProductDTOList) {
    this.collectionIdx = collection.getIdx();
    this.collectionName = collection.getName();
    this.collectionDescription = collection.getDescription();
    this.collectionThumbnailUrl = collection.getThumbnailUrl();
    this.collectionProductDTOList = collectionProductDTOList;
  }
}
