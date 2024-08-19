package clov3r.oneit_server.controller;

import clov3r.oneit_server.domain.DTO.CollectionDTO;
import clov3r.oneit_server.domain.DTO.ProductDTO;
import clov3r.oneit_server.domain.entity.Collection;
import clov3r.oneit_server.domain.entity.Product;
import clov3r.oneit_server.repository.CollectionRepository;
import clov3r.oneit_server.repository.KeywordRepository;
import clov3r.oneit_server.response.BaseResponse;
import clov3r.oneit_server.service.CollectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
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
  @Operation(summary = "컬렉션 상품 리스트 조회", description = "컬렉션에 속한 상품 리스트를 조회합니다.")
  @GetMapping("api/v1/collections/{collectionIdx}/products")
  public BaseResponse<List<ProductDTO>> getProductList(@Parameter(description = "컬렉션 idx") Long collectionIdx) {
    List<Product> productList = collectionRepository.getProductList(collectionIdx);
    List<ProductDTO> productDTOList = productList.stream().map(product -> new ProductDTO(
        product,
        keywordRepository.findKeywordByProductIdx(product.getIdx())
        )).toList();
    return new BaseResponse<>(productDTOList);
  }

}
