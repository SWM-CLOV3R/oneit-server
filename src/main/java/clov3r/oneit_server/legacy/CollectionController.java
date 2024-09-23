package clov3r.oneit_server.legacy;

import clov3r.oneit_server.domain.DTO.CollectionDTO;
import clov3r.oneit_server.domain.DTO.CollectionProductDTO;
import clov3r.oneit_server.domain.DTO.ProductDTO;
import clov3r.oneit_server.domain.entity.Collection;
import clov3r.oneit_server.domain.entity.Product;
import clov3r.oneit_server.repository.CollectionRepository;
import clov3r.oneit_server.repository.KeywordRepository;
import clov3r.oneit_server.legacy.response.BaseResponse;
import clov3r.oneit_server.service.CollectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CollectionController {
  private final CollectionService collectionService;
  private final CollectionRepository collectionRepository;
  private final KeywordRepository keywordRepository;

  @Tag(name = "컬렉션 API", description = "컬렉션(둘러보기) API")
  @Operation(summary = "컬렉션 리스트 조회", description = "컬렉션 리스트를 조회합니다.")
  @GetMapping("api/v1/collections")
  public BaseResponse<List<CollectionDTO>> getCollectionList() {
    List<Collection> collectionList = collectionRepository.getCollectionList();
    List<CollectionDTO> collectionDTOList = collectionList.stream().map(collection -> new CollectionDTO(
        collection.getIdx(),
        collection.getName(),
        collection.getDescription(),
        collection.getThumbnailUrl()
        )).toList();
    return new BaseResponse<>(collectionDTOList);
  }

  @Tag(name = "컬렉션 API", description = "컬렉션(둘러보기) API")
  @Operation(summary = "컬렉션 상세 조회", description = "컬렉션 정보와 컬렉션에 속한 상품 리스트를 조회합니다.")
  @GetMapping("api/v1/collections/{collectionIdx}")
  public BaseResponse<CollectionProductDTO> getProductList(
      @Parameter(description = "컬렉션 idx") @PathVariable Long collectionIdx
  ) {
    Collection collection = collectionRepository.getCollection(collectionIdx);
    List<Product> productList = collectionRepository.getProductList(collectionIdx);
    CollectionProductDTO collectionProductDTO = new CollectionProductDTO(
        collection.getIdx(),
        collection.getName(),
        collection.getDescription(),
        collection.getThumbnailUrl(),
        productList.stream().map(product -> new ProductDTO(
            product,
            keywordRepository.findKeywordByProductIdx(product.getIdx())
        )).toList()
    );
    return new BaseResponse<>(collectionProductDTO);
  }

}
