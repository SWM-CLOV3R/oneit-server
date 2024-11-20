package clov3r.api.product.service;

import clov3r.api.product.domain.dto.CollectionDetailDTO;
import clov3r.api.product.domain.dto.CollectionProductDTO;
import clov3r.api.product.repository.CollectionRepository;
import clov3r.api.product.repository.KeywordRepository;
import clov3r.domain.domains.entity.Collection;
import clov3r.domain.domains.entity.CollectionProduct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollectionService {
  private final CollectionRepository collectionRepository;
  private final KeywordRepository keywordRepository;
  public CollectionDetailDTO getCollectionDetail(Long collectionIdx) {
    Collection collection = collectionRepository.getCollection(collectionIdx);
    List<CollectionProduct> collectionProductList = collectionRepository.getCollectionProductList(collectionIdx);
    List<CollectionProductDTO> collectionProductDTOList = collectionProductList.stream().map(
        collectionProduct ->
        new CollectionProductDTO(
            collectionProduct.getProduct(),
            collectionProduct.getDetailImg()
        )).toList();
    return new CollectionDetailDTO(
        collection,
        collectionProductDTOList
    );
  }
}
