package clov3r.api.controller;

import static clov3r.api.error.errorcode.CommonErrorCode.*;
import static clov3r.api.error.errorcode.CustomErrorCode.*;

import clov3r.api.config.security.Auth;
import clov3r.api.domain.DTO.GiftboxProductDTO;
import clov3r.api.domain.collection.GiftboxProductVoteId;
import clov3r.api.domain.data.status.AccessStatus;
import clov3r.api.domain.data.status.VoteStatus;
import clov3r.api.domain.entity.Giftbox;
import clov3r.api.domain.entity.GiftboxProduct;
import clov3r.api.domain.entity.GiftboxProductVote;
import clov3r.api.domain.entity.Product;
import clov3r.api.domain.request.VoteProductRequest;
import clov3r.api.error.exception.BaseExceptionV2;
import clov3r.api.repository.GiftboxRepository;
import clov3r.api.repository.ProductRepository;
import clov3r.api.repository.UserRepository;
import clov3r.api.service.GiftboxProductService;
import clov3r.api.service.GiftboxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GiftboxProductControllerV2 {

  private final GiftboxService giftboxService;
  private final GiftboxRepository giftboxRepository;
  private final GiftboxProductService giftboxProductService;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;

  // 선물 바구니에 상품 리스트를 추가하는 API
  @Tag(name = "선물바구니 상품 API", description = "선물바구니 상품 CRUD API 목록")
  @Operation(summary = "선물바구니 상품 추가", description = "선물 바구니의 idx 리스트로 상품 추가")
  @PostMapping("/api/v2/giftbox/{giftboxIdx}/products")
  @Transactional
  public ResponseEntity<String> addProductToGiftbox(
      @PathVariable("giftboxIdx") Long giftboxIdx,
      @RequestBody List<Long> productIdxList,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    // request validation
    if (giftboxRepository.findById(giftboxIdx) == null) {
      throw new BaseExceptionV2(GIFTBOX_NOT_FOUND);
    }
    for (Long productIdx : productIdxList) {
      if (!productRepository.existsProduct(productIdx)) {
        throw new BaseExceptionV2(PRODUCT_NOT_FOUND);
      }
    }
    if (!userRepository.existsByUserIdx(userIdx)) {
      throw new BaseExceptionV2(USER_NOT_FOUND);
    }
    if (!giftboxRepository.isParticipantOfGiftbox(userIdx, giftboxIdx)) {
      throw new BaseExceptionV2(NOT_PARTICIPANT_OF_GIFTBOX);  // 해당 선물 바구니의 참여자만 상품 추가 가능함
    }

    // add product to giftbox
    for (Long productIdx : productIdxList) {
      giftboxService.addProductToGiftbox(giftboxIdx, productIdx);
    }
    return ResponseEntity.ok(giftboxIdx + "번 선물 바구니에 상품" +
        productIdxList + "이 추가되었습니다.");
  }

  // 선물 바구니의 상품 리스트 조회 API
  @Tag(name = "선물바구니 상품 API", description = "선물바구니 상품 CRUD API 목록")
  @Operation(summary = "선물바구니 상품 조회", description = "선물 바구니의 idx로 상품 조회")
  @GetMapping("/api/v2/giftbox/{giftboxIdx}/products")
  public ResponseEntity<List<GiftboxProductDTO>> getProductOfGiftbox(
      @PathVariable("giftboxIdx") Long giftboxIdx,
      @Parameter(hidden = true) @Auth(required = false) Long userIdx
  ) {
    // request validation
    if (giftboxIdx == null) {
      throw new BaseExceptionV2(REQUEST_ERROR);
    }
    if (giftboxRepository.findById(giftboxIdx) == null) {
      throw new BaseExceptionV2(GIFTBOX_NOT_FOUND);
    }

    // check if the user is a participant of the giftbox
    Giftbox giftbox = giftboxRepository.findById(giftboxIdx);
    if (giftbox.getAccessStatus().equals(AccessStatus.PRIVATE)) {
      if (userIdx == null || !giftboxRepository.isParticipantOfGiftbox(userIdx, giftboxIdx)) {
        throw new BaseExceptionV2(
            NOT_PARTICIPANT_OF_GIFTBOX);  // 선물 바구니가 PRIVATE 일 경우, 해당 선물 바구니의 참여자만 조회 가능함
      }
    }

    // get product list of the giftbox
    List<GiftboxProduct> giftboxProductList = giftboxRepository.findGiftboxProductList(
        giftboxIdx);

    List<GiftboxProductDTO> giftboxProductDTOList = giftboxProductList.stream()
        .map(giftboxProduct -> new GiftboxProductDTO(
            giftboxProduct.getProduct().getIdx(),
            giftboxProduct.getProduct().getName(),
            giftboxProduct.getProduct().getDescription(),
            giftboxProduct.getProduct().getOriginalPrice(),
            giftboxProduct.getProduct().getThumbnailUrl(),
            giftboxProduct.getLikeCount(),
            giftboxProductService.getVoteStatusOfUser(userIdx, giftboxIdx,
                giftboxProduct.getProduct().getIdx()),
            giftboxProduct.getPurchaseStatus()
        )).toList();

    return ResponseEntity.ok(giftboxProductDTOList);
  }

  // 선물 바구니의 상품 리스트 삭제 API
  @Tag(name = "선물바구니 상품 API", description = "선물바구니 상품 CRUD API 목록")
  @Operation(summary = "선물바구니 상품 삭제", description = "선물 바구니의 idx와 상품 idx 리스트로 상품 삭제")
  @DeleteMapping("/api/v2/giftbox/{giftboxIdx}/products")
  public ResponseEntity<String> deleteProductsOfGiftbox(
      @PathVariable("giftboxIdx") Long giftboxIdx,
      @RequestBody List<Long> productIdxList,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    // request validation
    if (giftboxIdx == null || productIdxList == null || userIdx == null) {
      throw new BaseExceptionV2(REQUEST_ERROR);
    }
    if (giftboxRepository.findById(giftboxIdx) == null) {
      throw new BaseExceptionV2(GIFTBOX_NOT_FOUND);
    }
    if (!userRepository.existsByUserIdx(userIdx)) {
      throw new BaseExceptionV2(USER_NOT_FOUND);
    }
    if (!giftboxRepository.isParticipantOfGiftbox(userIdx, giftboxIdx)) {
      throw new BaseExceptionV2(NOT_PARTICIPANT_OF_GIFTBOX);  // 해당 선물 바구니의 참여자만 상품 삭제 가능함
    }
    for (Long productIdx : productIdxList) {
      if (!productRepository.existsProduct(productIdx)) {
        throw new BaseExceptionV2(PRODUCT_NOT_FOUND);
      }
    }

    // delete products of the giftbox
    for (Long productIdx : productIdxList) {
      try {
        giftboxRepository.deleteProductOfGiftbox(giftboxIdx, productIdx);
      } catch (BaseExceptionV2 e) {
        throw new BaseExceptionV2(PRODUCT_NOT_FOUND);
      }
    }
    return ResponseEntity.ok(giftboxIdx + "번 선물 바구니의 상품" +
        productIdxList
        + "이 삭제되었습니다.");
  }

  @Transactional
  @Tag(name = "선물바구니 상품 API", description = "선물바구니 API 목록")
  @Operation(summary = "선물 바구니 상품 좋아요/싫어요 투표", description = "선물 바구니 상품에 좋아요/싫어요 투표합니다.")
  @PutMapping("/api/v2/giftbox/products/vote")
  public ResponseEntity<String> voteProduct(
      @RequestBody VoteProductRequest request,
      @Parameter(hidden = true) @Auth(required = false) Long userIdx
  ) {

    // request validation
    if (request.getGiftboxIdx() == null || request.getProductIdx() == null
        || request.getBrowserUuid() == null || request.getVote() == null) {
      throw new BaseExceptionV2(REQUEST_ERROR);
    }
    if (!giftboxRepository.existsById(request.getGiftboxIdx())) {
      throw new BaseExceptionV2(GIFTBOX_NOT_FOUND);
    }
    if (!productRepository.existsProduct(request.getProductIdx())) {
      throw new BaseExceptionV2(PRODUCT_NOT_FOUND);
    }
    List<Product> products = giftboxRepository.findProductOfGiftbox(request.getGiftboxIdx());
    if (products.stream().noneMatch(product -> product.getIdx().equals(request.getProductIdx()))) {
      throw new BaseExceptionV2(PRODUCT_NOT_FOUND);
    }

//    if (userIdx == null) {
//      userIdx = Long.valueOf(UUID.randomUUID().toString().substring(0, 10));
//    }

    // 로그인 유저만 가능한 경우
    if (userIdx == null || !userRepository.existsByUserIdx(userIdx)) {
      throw new BaseExceptionV2(USER_NOT_FOUND);
    }
    if (!giftboxRepository.isParticipantOfGiftbox(userIdx, request.getGiftboxIdx())) {
      throw new BaseExceptionV2(NOT_PARTICIPANT_OF_GIFTBOX);  // 해당 선물 바구니의 참여자만 투표 가능함
    }

    // vote product
    GiftboxProductVote newVote = new GiftboxProductVote(
        new GiftboxProductVoteId(
            request.getGiftboxIdx(),
            request.getProductIdx(),
            userIdx,
            request.getBrowserUuid()
        ),
        request.getVote()
    );
    // vote
    VoteStatus previousStatus = giftboxProductService.voteProduct(newVote);
    // count update
    giftboxProductService.updateVoteCount(request.getGiftboxIdx(), request.getProductIdx(),
        previousStatus, newVote.getVote());
    return ResponseEntity.ok("상품에 투표하였습니다.");
  }

  @Tag(name = "선물바구니 상품 API", description = "선물바구니 API 목록")
  @Operation(summary = "선물바구니 제품 구매 표시", description = "선물바구니 제품의 구매했음을 표시합니다.")
  @PutMapping("/api/v2/giftbox/{giftboxIdx}/products/{productIdx}/purchase")
  public ResponseEntity<String> purchaseProduct(
      @PathVariable("giftboxIdx") Long giftboxIdx,
      @PathVariable("productIdx") Long productIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    // request validation
    if (giftboxIdx == null || productIdx == null || userIdx == null) {
      throw new BaseExceptionV2(REQUEST_ERROR);
    }
    if (!giftboxRepository.existsById(giftboxIdx)) {
      throw new BaseExceptionV2(GIFTBOX_NOT_FOUND);
    }
    if (!productRepository.existsProduct(productIdx)) {
      throw new BaseExceptionV2(PRODUCT_NOT_FOUND);
    }
    if (!userRepository.existsByUserIdx(userIdx)) {
      throw new BaseExceptionV2(USER_NOT_FOUND);
    }
    if (!giftboxRepository.isParticipantOfGiftbox(userIdx, giftboxIdx)) {
      throw new BaseExceptionV2(NOT_PARTICIPANT_OF_GIFTBOX);  // 해당 선물 바구니의 참여자만 구매 표시 가능함
    }

    // purchase product
    giftboxProductService.purchaseProduct(giftboxIdx, productIdx);
    return ResponseEntity.ok("제품을 구매했습니다.");
  }

}
