package clov3r.api.product.service;

import clov3r.api.product.domain.dto.CollectionDetailDTO;
import clov3r.api.product.domain.dto.CollectionProductDTO;
import clov3r.api.product.domain.entity.Collection;
import clov3r.api.product.domain.entity.CollectionProduct;
import clov3r.api.product.repository.CollectionRepository;
import clov3r.api.product.repository.KeywordRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollectionService {
  private final CollectionRepository collectionRepository;
  private final KeywordRepository keywordRepository;
  public CollectionDetailDTO getCollectionDetail(Long collectionIdx, Long userIdx) {
    Collection collection = collectionRepository.getCollection(collectionIdx);
    List<CollectionProduct> collectionProductList = collectionRepository.getCollectionProductList(collectionIdx);
    List<CollectionProductDTO> collectionProductDTOList = collectionProductList.stream().map(
        collectionProduct ->
        new CollectionProductDTO(
            collectionProduct.getProduct().getIdx(),
            collectionProduct.getProduct().getName(),
            collectionProduct.getProduct().getKeyword(),
            collectionProduct.getDetailImg(),
            collectionProduct.getProduct().getStatus()
        )).toList();
    return new CollectionDetailDTO(
        collection,
        collectionProductDTOList
    );
  }
}
