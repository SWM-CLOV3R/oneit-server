package clov3r.oneit_server.controller;

import static clov3r.oneit_server.response.BaseResponseStatus.*;

import clov3r.oneit_server.config.security.Auth;
import clov3r.oneit_server.domain.DTO.GiftboxDTO;
import clov3r.oneit_server.domain.DTO.ProductSummaryDTO;
import clov3r.oneit_server.domain.data.AccessStatus;
import clov3r.oneit_server.domain.entity.Product;
import clov3r.oneit_server.domain.request.PostGiftboxRequest;
import clov3r.oneit_server.domain.entity.Giftbox;
import clov3r.oneit_server.repository.GiftboxRepository;
import clov3r.oneit_server.repository.ProductRepository;
import clov3r.oneit_server.repository.UserRepository;
import clov3r.oneit_server.response.BaseResponse;
import clov3r.oneit_server.exception.BaseException;
import clov3r.oneit_server.service.GiftboxService;
import clov3r.oneit_server.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class GiftboxController {

  private final GiftboxService giftboxService;
  private final GiftboxRepository giftboxRepository;
  private final S3Service s3Service;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;

  @Tag(name = "Giftbox API", description = "Giftbox CRUD API 목록")
  @Operation(summary = "Giftbox 생성", description = "선물 바구니 생성, 이미지는 선택적으로 업로드 가능, 이미지를 업로드하지 않을 경우 null로 저장")
  @PostMapping(value = "/api/v1/giftbox", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public BaseResponse<Long> createGiftbox(
      @RequestPart("request") PostGiftboxRequest request,
      @RequestPart(value = "image", required = false) MultipartFile image,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    try {
      // request validation
      if (request.getName() == null || request.getDeadline() == null || userIdx == null) {
        return new BaseResponse<>(REQUEST_ERROR);
      }
      if (!userRepository.existsUser(userIdx)) {
        return new BaseResponse<>(USER_NOT_FOUND);
      }
      if (request.getDeadline().isBefore(LocalDateTime.now().toLocalDate())) {
        return new BaseResponse<>(DATE_BEFORE_NOW);
      }
      if (!request.getAccessStatus().equals(AccessStatus.PUBLIC.toString())
          && !request.getAccessStatus().equals(AccessStatus.PRIVATE.toString())) {
        return new BaseResponse<>(INVALID_ACCESS_STATUS);
      }

      // create Gift box
      Long giftboxIdx = giftboxService.createGiftbox(request, userIdx);

      // upload image if exists
      if (image != null) {
        String imageUrl = s3Service.upload(image, "giftbox-profile");
        giftboxService.updateGiftboxImageUrl(giftboxIdx, imageUrl);
      }
      return new BaseResponse<>(giftboxIdx);

    } catch (BaseException e) {
      return new BaseResponse<>(e.getBaseResponseStatus());
    }
  }

  @Tag(name = "Giftbox API", description = "Giftbox CRUD API 목록")
  @Operation(summary = "Giftbox 상세 조회", description = "선물 바구니의 idx로 선물 바구니 조회, 삭제한 선물 바구니는 조회되지 않음")
  @GetMapping("/api/v1/giftbox/{giftboxIdx}")
  public BaseResponse<GiftboxDTO> getGiftboxByIdx(
      @PathVariable("giftboxIdx") Long giftboxIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  )
  {
    // request validation
    if (giftboxIdx == null || userIdx == null) {
      return new BaseResponse<>(REQUEST_ERROR);
    }
    if (!giftboxRepository.existsById(giftboxIdx)) {
      return new BaseResponse<>(GIFTBOX_NOT_FOUND);
    }
    if (!userRepository.existsUser(userIdx)) {
      return new BaseResponse<>(USER_NOT_FOUND);
    }

    // get giftbox
    try {
      Giftbox giftbox = giftboxRepository.findById(giftboxIdx);

      if (giftbox.getAccessStatus().equals(AccessStatus.PRIVATE) && !giftboxRepository.isParticipantOfGiftbox(userIdx, giftboxIdx)) {
        return new BaseResponse<>(NOT_PARTICIPANT_OF_GIFTBOX);  // 선물 바구니가 PRIVATE 일 경우, 해당 선물 바구니의 참여자만 조회 가능함
      }

      GiftboxDTO giftboxDTO = new GiftboxDTO(
          giftbox.getIdx(),
          giftbox.getName(),
          giftbox.getDescription(),
          giftbox.getDeadline(),
          giftbox.getImageUrl(),
          giftbox.getCreatedUserIdx(),
          giftbox.getAccessStatus()
      );
      return new BaseResponse<>(giftboxDTO);
    } catch (BaseException e) {
      return new BaseResponse<>(e.getBaseResponseStatus());
    }
  }

  @Tag(name = "Giftbox API", description = "Giftbox CRUD API 목록")
  @Operation(summary = "Giftbox 목록 조회(추후 삭제 예정)", description = "모든 선물 바구니 조회, 삭제한 선물 바구니는 조회되지 않음")
  @GetMapping("/api/v1/giftbox/all")
  public BaseResponse<List<GiftboxDTO>> getGiftboxList() {
    try {
      List<Giftbox> giftboxList = giftboxRepository.findAll();
      List<GiftboxDTO> giftboxDTOList = giftboxList.stream()
          .map(GiftboxDTO::new)
          .toList();
      return new BaseResponse<>(giftboxDTOList);
    } catch (BaseException e) {
      return new BaseResponse<>(DATABASE_ERROR);
    }
  }

  @Tag(name = "Giftbox API", description = "Giftbox CRUD API 목록")
  @Operation(summary = "해당 유저의 Giftbox 목록 조회", description = "해당 유저가 소유한 모든 선물 바구니 조회, 삭제한 선물 바구니는 조회되지 않음")
  @GetMapping("/api/v1/giftbox")
  public BaseResponse<List<GiftboxDTO>> getGiftboxList(
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    // request validation
    if (!userRepository.existsUser(userIdx)) {
      return new BaseResponse<>(USER_NOT_FOUND);
    }

    // get giftbox list of the user
    try {
      List<Giftbox> giftboxList = giftboxRepository.findGiftboxOfUser(userIdx);
      List<GiftboxDTO> giftboxDTOList = giftboxList.stream()
          .map(GiftboxDTO::new)
          .toList();
      return new BaseResponse<>(giftboxDTOList);
    } catch (BaseException e) {
      return new BaseResponse<>(DATABASE_ERROR);
    }
  }

  @Tag(name = "Giftbox API", description = "Giftbox CRUD API 목록")
  @Operation(summary = "Giftbox 삭제", description = "선물 바구니의 idx로 선물 바구니 삭제")
  @DeleteMapping("/api/v1/giftbox/{giftboxIdx}")
  public BaseResponse<String> deleteGiftboxByIdx(
      @PathVariable("giftboxIdx") Long giftboxIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    // request validation
    if (giftboxRepository.findById(giftboxIdx) == null) {
      return new BaseResponse<>(GIFTBOX_NOT_FOUND);
    }
    if (!userRepository.existsUser(userIdx)) {
      return new BaseResponse<>(USER_NOT_FOUND);
    }
    if (!giftboxRepository.isManagerOfGiftbox(userIdx, giftboxIdx)) {
      return new BaseResponse<>(NOT_MANAGER_OF_GIFTBOX);  // 해당 선물 바구니의 관리자만 삭제 가능함
    }

    // delete giftbox
    try {
      giftboxRepository.deleteById(giftboxIdx);
      return new BaseResponse<>(giftboxIdx + "번 선물 바구니가 삭제되었습니다.");
    } catch (BaseException e) {
      return new BaseResponse<>(DATABASE_ERROR);
    }
  }

  @Tag(name = "Giftbox API", description = "Giftbox CRUD API 목록")
  @Operation(summary = "Giftbox 수정", description = "선물 바구니의 idx로 선물 바구니 수정, 이미지는 선택적으로 업로드 가능, 이미지를 업로드하지 않을 경우 null로 저장")
  @PutMapping(value = "/api/v1/giftbox/{giftboxIdx}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public BaseResponse<Long> updateGiftbox(
      @PathVariable("giftboxIdx") Long giftboxIdx,
      @RequestPart("request") PostGiftboxRequest request,
      @RequestPart(value = "image", required = false) MultipartFile image,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    try {
      // request validation
      if (giftboxRepository.findById(giftboxIdx) == null) {
        return new BaseResponse<>(GIFTBOX_NOT_FOUND);
      }
      if (request.getName() == null || request.getDeadline() == null || userIdx == null) {
        return new BaseResponse<>(REQUEST_ERROR);
      }
      if (!userRepository.existsUser(userIdx)) {
        return new BaseResponse<>(USER_NOT_FOUND);
      }
      if (!giftboxRepository.isManagerOfGiftbox(userIdx, giftboxIdx)) {
        return new BaseResponse<>(NOT_MANAGER_OF_GIFTBOX);  // 해당 선물 바구니의 관리자만 수정 가능함
      }
      if (request.getDeadline().isBefore(LocalDateTime.now().toLocalDate())) {
        return new BaseResponse<>(DATE_BEFORE_NOW);
      }
      if (!request.getAccessStatus().equals(AccessStatus.PUBLIC.toString())
          && !request.getAccessStatus().equals(AccessStatus.PRIVATE.toString())) {
        return new BaseResponse<>(INVALID_ACCESS_STATUS);
      }

      // update giftbox
      giftboxService.updateGiftbox(giftboxIdx, request);

      // upload image if exists
      if (image != null) {
        String imageUrl = s3Service.upload(image, "giftbox-profile");
        giftboxService.updateGiftboxImageUrl(giftboxIdx, imageUrl);
      }
      return new BaseResponse<>(giftboxIdx);

    } catch (BaseException e) {
      return new BaseResponse<>(e.getBaseResponseStatus());
    }
  }

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
  public BaseResponse<List<ProductSummaryDTO>> getProductOfGiftbox(
      @PathVariable("giftboxIdx") Long giftboxIdx
  ) {
    // request validation
    if (giftboxRepository.findById(giftboxIdx) == null) {
      return new BaseResponse<>(GIFTBOX_NOT_FOUND);
    }

    // get product list of the giftbox
    List<Product> productList = giftboxRepository.findProductOfGiftbox(giftboxIdx);
    List<ProductSummaryDTO> productSummaryDTOList = productList.stream()
        .map(ProductSummaryDTO::new)
        .toList();
    return new BaseResponse<>(productSummaryDTOList);
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
}
