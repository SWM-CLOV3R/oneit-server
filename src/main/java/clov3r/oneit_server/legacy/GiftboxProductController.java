package clov3r.oneit_server.legacy;

import static clov3r.oneit_server.legacy.response.BaseResponseStatus.GIFTBOX_NOT_FOUND;
import static clov3r.oneit_server.legacy.response.BaseResponseStatus.NOT_PARTICIPANT_OF_GIFTBOX;
import static clov3r.oneit_server.legacy.response.BaseResponseStatus.PRODUCT_NOT_FOUND;
import static clov3r.oneit_server.legacy.response.BaseResponseStatus.REQUEST_ERROR;
import static clov3r.oneit_server.legacy.response.BaseResponseStatus.USER_NOT_FOUND;

import clov3r.oneit_server.config.security.Auth;
import clov3r.oneit_server.domain.DTO.GiftboxProductDTO;
import clov3r.oneit_server.domain.collection.GiftboxProductVoteId;
import clov3r.oneit_server.domain.data.status.AccessStatus;
import clov3r.oneit_server.domain.data.status.VoteStatus;
import clov3r.oneit_server.domain.entity.Giftbox;
import clov3r.oneit_server.domain.entity.GiftboxProduct;
import clov3r.oneit_server.domain.entity.GiftboxProductVote;
import clov3r.oneit_server.domain.entity.Product;
import clov3r.oneit_server.domain.request.VoteProductRequest;
import clov3r.oneit_server.legacy.exception.BaseException;
import clov3r.oneit_server.repository.GiftboxProductRepository;
import clov3r.oneit_server.repository.GiftboxRepository;
import clov3r.oneit_server.repository.ProductRepository;
import clov3r.oneit_server.repository.UserRepository;
import clov3r.oneit_server.legacy.response.BaseResponse;
import clov3r.oneit_server.service.GiftboxProductService;
import clov3r.oneit_server.service.GiftboxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GiftboxProductController {

  private final GiftboxService giftboxService;
  private final GiftboxRepository giftboxRepository;
  private final GiftboxProductRepository giftboxProductRepository;
  private final GiftboxProductService giftboxProductService;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;

  // 선물 바구니에 상품 리스트를 추가하는 API
  @Tag(name = "Giftbox 상품 API", description = "Giftbox 상품 CRUD API 목록")
  @Operation(summary = "Giftbox 상품 추가", description = "선물 바구니의 idx 리스트로 상품 추가")
  @PostMapping("/api/v1/giftbox/{giftboxIdx}/products")
  @Transactional
  public BaseResponse<String> addProductToGiftbox(
      @PathVariable("giftboxIdx") Long giftboxIdx,
      @RequestBody List<Long> productIdxList,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    // request validation
    if (giftboxRepository.findById(giftboxIdx) == null) {
      return new BaseResponse<>(GIFTBOX_NOT_FOUND);
    }
    for(Long productIdx : productIdxList) {
      if (!productRepository.existsProduct(productIdx)) {
        return new BaseResponse<>(PRODUCT_NOT_FOUND);
      }
    }
    if (!userRepository.existsUser(userIdx)) {
      return new BaseResponse<>(USER_NOT_FOUND);
    }
    if (!giftboxRepository.isParticipantOfGiftbox(userIdx, giftboxIdx)) {
      return new BaseResponse<>(NOT_PARTICIPANT_OF_GIFTBOX);  // 해당 선물 바구니의 참여자만 상품 추가 가능함
    }

    // add product to giftbox
    try {
      for (Long productIdx : productIdxList) {
        giftboxService.addProductToGiftbox(giftboxIdx, productIdx);
      }
    } catch (BaseException e) {
      return new BaseResponse<>(e.getBaseResponseStatus());
    }
    return new BaseResponse<>(giftboxIdx + "번 선물 바구니에 상품" +
        productIdxList.toString() + "이 추가되었습니다.");
  }

  // 선물 바구니의 상품 리스트 조회 API
  @Tag(name = "Giftbox 상품 API", description = "Giftbox 상품 CRUD API 목록")
  @Operation(summary = "Giftbox 상품 조회", description = "선물 바구니의 idx로 상품 조회")
  @GetMapping("/api/v1/giftbox/{giftboxIdx}/products")
  public BaseResponse<List<GiftboxProductDTO>> getProductOfGiftbox(
      @PathVariable("giftboxIdx") Long giftboxIdx,
      @Parameter(hidden = true) @Auth(required = false) Long userIdx
  ) {
    // request validation
    if (giftboxIdx == null) {
      return new BaseResponse<>(REQUEST_ERROR);
    }
    if (giftboxRepository.findById(giftboxIdx) == null) {
      return new BaseResponse<>(GIFTBOX_NOT_FOUND);
    }

    try {
      // check if the user is a participant of the giftbox
      Giftbox giftbox = giftboxRepository.findById(giftboxIdx);
      if (giftbox.getAccessStatus().equals(AccessStatus.PRIVATE)) {
        if (userIdx == null || !giftboxRepository.isParticipantOfGiftbox(userIdx, giftboxIdx)) {
          return new BaseResponse<>(
              NOT_PARTICIPANT_OF_GIFTBOX);  // 선물 바구니가 PRIVATE 일 경우, 해당 선물 바구니의 참여자만 조회 가능함
        }
      }

      // get product list of the giftbox
      List<GiftboxProduct> giftboxProductList = giftboxRepository.findGiftboxProductList(giftboxIdx);


      List<GiftboxProductDTO> giftboxProductDTOList = giftboxProductList.stream()
          .map(giftboxProduct -> new GiftboxProductDTO(
              giftboxProduct.getProduct().getIdx(),
              giftboxProduct.getProduct().getName(),
              giftboxProduct.getProduct().getDescription(),
              giftboxProduct.getProduct().getOriginalPrice(),
              giftboxProduct.getProduct().getCurrentPrice(),
              giftboxProduct.getProduct().getDiscountRate(),
              giftboxProduct.getProduct().getThumbnailUrl(),
              giftboxProduct.getLikeCount(),
              giftboxProductService.getVoteStatusOfUser(userIdx, giftboxIdx, giftboxProduct.getProduct().getIdx())
          )).toList();

      return new BaseResponse<>(giftboxProductDTOList);
    } catch (BaseException e) {
      return new BaseResponse<>(e.getBaseResponseStatus());
    }
  }

  // 선물 바구니의 상품 리스트 삭제 API
  @Tag(name = "Giftbox 상품 API", description = "Giftbox 상품 CRUD API 목록")
  @Operation(summary = "Giftbox 상품 삭제", description = "선물 바구니의 idx와 상품 idx 리스트로 상품 삭제")
  @DeleteMapping("/api/v1/giftbox/{giftboxIdx}/products")
  public BaseResponse<String> deleteProductsOfGiftbox(
      @PathVariable("giftboxIdx") Long giftboxIdx,
      @RequestBody List<Long> productIdxList,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    // request validation
    if (giftboxIdx == null || productIdxList == null || userIdx == null) {
      return new BaseResponse<>(REQUEST_ERROR);
    }
    if (giftboxRepository.findById(giftboxIdx) == null) {
      return new BaseResponse<>(GIFTBOX_NOT_FOUND);
    }
    if (!userRepository.existsUser(userIdx)) {
      return new BaseResponse<>(USER_NOT_FOUND);
    }
    if (!giftboxRepository.isParticipantOfGiftbox(userIdx, giftboxIdx)) {
      return new BaseResponse<>(NOT_PARTICIPANT_OF_GIFTBOX);  // 해당 선물 바구니의 참여자만 상품 삭제 가능함
    }
    for(Long productIdx : productIdxList) {
      if (!productRepository.existsProduct(productIdx)) {
        return new BaseResponse<>(PRODUCT_NOT_FOUND);
      }
    }

    // delete products of the giftbox
    for (Long productIdx : productIdxList) {
      try {
        giftboxRepository.deleteProductOfGiftbox(giftboxIdx, productIdx);
      } catch (BaseException e) {
        return new BaseResponse<>(PRODUCT_NOT_FOUND);
      }
    }
    return new BaseResponse<>(giftboxIdx + "번 선물 바구니의 상품"+
        productIdxList.toString()
        +"이 삭제되었습니다.");
  }

  @Transactional
  @Tag(name = "Giftbox 상품 API", description = "Giftbox API 목록")
  @Operation(summary = "선물 바구니 상품 좋아요/싫어요 투표", description = "선물 바구니 상품에 좋아요/싫어요 투표합니다.")
  @PutMapping("/api/v1/giftbox/products/vote")
  public BaseResponse<String> voteProduct(
      @RequestBody VoteProductRequest request,
      @Parameter(hidden = true) @Auth(required = false) Long userIdx
  ) {

    // request validation
    if (request.getGiftboxIdx() == null || request.getProductIdx() == null || request.getBrowserUuid() == null || request.getVote() == null) {
      return new BaseResponse<>(REQUEST_ERROR);
    }
    if (!giftboxRepository.existsById(request.getGiftboxIdx())) {
      return new BaseResponse<>(GIFTBOX_NOT_FOUND);
    }
    if (!productRepository.existsProduct(request.getProductIdx())) {
      return new BaseResponse<>(PRODUCT_NOT_FOUND);
    }
    List<Product> products = giftboxRepository.findProductOfGiftbox(request.getGiftboxIdx());
    if (products.stream().noneMatch(product -> product.getIdx().equals(request.getProductIdx()))) {
      return new BaseResponse<>(PRODUCT_NOT_FOUND);
    }

//    if (userIdx == null) {
//      userIdx = Long.valueOf(UUID.randomUUID().toString().substring(0, 10));
//    }

    // 로그인 유저만 가능한 경우
    if (userIdx == null || !userRepository.existsUser(userIdx)) {
      return new BaseResponse<>(USER_NOT_FOUND);
    }
    if (!giftboxRepository.isParticipantOfGiftbox(userIdx, request.getGiftboxIdx())) {
      return new BaseResponse<>(NOT_PARTICIPANT_OF_GIFTBOX);  // 해당 선물 바구니의 참여자만 투표 가능함
    }

    try {
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
      giftboxProductService.updateVoteCount(request.getGiftboxIdx(), request.getProductIdx(), previousStatus, newVote.getVote());
      return new BaseResponse<>("상품에 투표하였습니다.");
    } catch (BaseException e) {
      return new BaseResponse<>(e.getBaseResponseStatus());
    }
  }
}
