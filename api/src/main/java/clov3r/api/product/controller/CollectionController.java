package clov3r.api.product.controller;

import clov3r.api.product.domain.dto.CollectionDTO;
import clov3r.api.auth.security.Auth;
import clov3r.api.product.domain.dto.CollectionDetailDTO;
import clov3r.api.product.repository.CollectionRepository;
import clov3r.api.product.service.CollectionService;
import clov3r.domain.domains.entity.Collection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CollectionController {
  private final CollectionService collectionService;
  private final CollectionRepository collectionRepository;

  @Tag(name = "컬렉션 API", description = "컬렉션(둘러보기) API")
  @Operation(summary = "컬렉션 리스트 조회", description = "컬렉션 리스트를 조회합니다.")
  @GetMapping("api/v2/collections")
  public ResponseEntity<List<CollectionDTO>> getCollectionList() {
    List<Collection> collectionList = collectionRepository.getCollectionList();
    List<CollectionDTO> collectionDTOList = collectionList.stream().map(collection -> new CollectionDTO(
        collection.getIdx(),
        collection.getName(),
        collection.getDescription(),
        collection.getThumbnailUrl()
        )).toList();
    return ResponseEntity.ok(collectionDTOList);
  }

  @Tag(name = "컬렉션 API", description = "컬렉션(둘러보기) API")
  @Operation(summary = "컬렉션 상세 조회", description = "컬렉션 정보와 컬렉션에 속한 상품 리스트를 조회합니다.")
  @GetMapping("api/v2/collections/{collectionIdx}")
  public ResponseEntity<CollectionDetailDTO> getProductList(
      @Parameter(description = "컬렉션 idx") @PathVariable Long collectionIdx,
      @Parameter(hidden = true) @Auth(required = false) Long userIdx
  ) {
    CollectionDetailDTO collectionDetailDTO = collectionService.getCollectionDetail(collectionIdx);
    return ResponseEntity.ok(collectionDetailDTO);
  }
}
