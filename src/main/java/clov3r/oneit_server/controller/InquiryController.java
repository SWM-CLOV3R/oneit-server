package clov3r.oneit_server.controller;

import static clov3r.oneit_server.error.errorcode.CustomErrorCode.*;

import clov3r.oneit_server.config.security.Auth;
import clov3r.oneit_server.domain.DTO.InquiryDTO;
import clov3r.oneit_server.domain.DTO.InquiryProductDTO;
import clov3r.oneit_server.domain.DTO.InquiryResultDTO;
import clov3r.oneit_server.domain.DTO.ProductSummaryDTO;
import clov3r.oneit_server.domain.data.ProductEmoji;
import clov3r.oneit_server.domain.data.status.AccessStatus;
import clov3r.oneit_server.domain.entity.Giftbox;
import clov3r.oneit_server.domain.entity.Inquiry;
import clov3r.oneit_server.domain.entity.Product;
import clov3r.oneit_server.domain.request.InquiryRequest;
import clov3r.oneit_server.error.errorcode.CustomErrorCode;
import clov3r.oneit_server.error.exception.BaseExceptionV2;
import clov3r.oneit_server.repository.GiftboxRepository;
import clov3r.oneit_server.repository.InquiryRepository;
import clov3r.oneit_server.repository.ProductRepository;
import clov3r.oneit_server.service.InquiryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InquiryController {

  private final GiftboxRepository giftboxRepository;
  private final InquiryService inquiryService;
  private final InquiryRepository inquiryRepository;
  private final ProductRepository productRepository;

  @Tag(name = "물어보기 API", description = "물어보기 API 목록")
  @Operation(summary = "물어보기 생성", description = "물어보기를 생성합니다.")
  @PostMapping("/api/v2/inquiry")
  public ResponseEntity<Long> createInquiry(
      @RequestBody InquiryRequest inquiryRequest,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    // user validation (only giftbox manager)
    if (!giftboxRepository.isManagerOfGiftbox(userIdx, inquiryRequest.getGiftboxIdx())) {
      throw new BaseExceptionV2(NOT_MANAGER_OF_GIFTBOX);  // 해당 선물 바구니의 관리자만 물어보기 가능함
    }
    // product validation
    for (Long productIdx : inquiryRequest.getProductIdxList()) {
      if (!productRepository.existsProduct(productIdx)) {
        throw new BaseExceptionV2(PRODUCT_NOT_FOUND);
      }
    }

    // giftbox validation
    if (giftboxRepository.findById(inquiryRequest.getGiftboxIdx()) == null) {
      throw new BaseExceptionV2(GIFTBOX_NOT_FOUND);
    }

    // make inquiry
    Long idx = inquiryService.createInquiry(inquiryRequest, userIdx);
    for (Long productIdx : inquiryRequest.getProductIdxList()) {
      Inquiry inquiry = inquiryRepository.findByIdx(idx);
      Product product = productRepository.findById(productIdx);
      inquiryRepository.addProductToInquiry(inquiry, product);
    }

    return ResponseEntity.ok(idx);
  }

  @Tag(name = "물어보기 API", description = "물어보기 API 목록")
  @Operation(summary = "물어보기 조회", description = "물어보기 내용을 조회합니다.")
  @GetMapping("/api/v2/inquiry/{inquiryIdx}")
  public ResponseEntity<InquiryDTO> getInquiry(
      @PathVariable("inquiryIdx") Long inquiryIdx,
      @Parameter(hidden = true) @Auth(required = false) Long userIdx
  ) {

    Inquiry inquiry = inquiryRepository.findByIdx(inquiryIdx);
    List<Product> productList = inquiryRepository.findProductListByInquiry(inquiry);
    InquiryDTO inquiryDTO = new InquiryDTO(inquiry,
        productList.stream().map(ProductSummaryDTO::new).toList());

    return ResponseEntity.ok(inquiryDTO);
  }

  @Tag(name = "물어보기 API", description = "물어보기 API 목록")
  @Operation(summary = "물어보기 이모지 추가", description = "물어보기 제품에 대해 이모지를 추가합니다. 계정 정보는 받지 않습니다.")
  @PostMapping("/api/v2/inquiry/{inquiryIdx}/emoji")
  public ResponseEntity<String> addEmoji(
      @PathVariable("inquiryIdx") Long inquiryIdx,
      @RequestBody List<ProductEmoji> productEmojiList
  ) {
    if (productEmojiList.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    if (productEmojiList.stream().anyMatch(
        productEmoji -> productRepository.findById(productEmoji.getProductIdx()) == null)) {
      throw new BaseExceptionV2(PRODUCT_NOT_FOUND);
    }
    // check if all emoji of productEmojiList exists
    if (productEmojiList.stream()
        .anyMatch(productEmoji -> !inquiryRepository.existEmojiById(productEmoji.getEmojiIdx()))) {
      throw new BaseExceptionV2(NOT_EXIST_EMOJI);
    }
    if (!inquiryRepository.existInquiry(inquiryIdx)) {
      throw new BaseExceptionV2(NOT_EXIST_INQUIRY);
    }

    if (productEmojiList.stream().anyMatch(
        productEmoji -> !inquiryRepository.existInquiryProduct(inquiryIdx,
            productEmoji.getProductIdx()))) {
      throw new BaseExceptionV2(NOT_EXIST_INQUIRY_PRODUCT);
    }

    inquiryService.addEmoji(inquiryIdx, productEmojiList);
    return ResponseEntity.ok().build();
  }

  @Tag(name = "물어보기 API", description = "물어보기 API 목록")
  @Operation(summary = "선물바구니의 물어보기 결과 조회(이모지 포함)", description = "선물바구니의 물어보기 결과를 조회합니다.(각 제품별 이모지 포함, 여러 물어보기 합산)")
  @GetMapping("/api/v2/inquiry/{inquiryIdx}/emoji")
  public ResponseEntity<InquiryResultDTO> getInquiryResult(
      @PathVariable("inquiryIdx") Long inquiryIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    Inquiry inquiry = inquiryRepository.findByIdx(inquiryIdx);
    if (inquiry == null) {
      throw new BaseExceptionV2(CustomErrorCode.INQUIRY_NOT_FOUND);
    }
    // 물어보기 조회는 해당 선물바구니의 참여자만 가능하다
    if (!giftboxRepository.isParticipantOfGiftbox(userIdx, inquiry.getGiftboxIdx())) {
      throw new BaseExceptionV2(NOT_PARTICIPANT_OF_GIFTBOX);  // 선물 바구니가 PRIVATE일 경우 해당 선물 바구니의 참여자만 조회 가능함
    }
    List<Product> productList = inquiryRepository.findProductListByInquiry(inquiry);
    List<InquiryProductDTO> inquiryProductDTOS = productList.stream().map(product ->
        new InquiryProductDTO(
            product,
            inquiryRepository.findEmojiByInquiryAndProduct(inquiryIdx, product.getIdx()))
    ).toList();

    InquiryResultDTO inquiryReulstDTO = new InquiryResultDTO(
        inquiry,
        inquiryProductDTOS);

    return ResponseEntity.ok(inquiryReulstDTO);
  }

  @Tag(name = "물어보기 API", description = "물어보기 API 목록")
  @Operation(summary = "물어보기 완료", description = "물어보기를 완료하고, 물어보기 상태를 비활성화합니다.")
  @PatchMapping("/api/v2/inquiry/{inquiryIdx}/complete")
  public ResponseEntity<String> completeInquiry(
      @PathVariable("inquiryIdx") Long inquiryIdx,
      @Parameter(hidden = true) @Auth Long userIdx
  ) {
    Inquiry inquiry = inquiryRepository.findByIdx(inquiryIdx);
    if (inquiry == null) {
      throw new BaseExceptionV2(CustomErrorCode.INQUIRY_NOT_FOUND);
    }
    // 물어보기 완료는 해당 선물바구니의 관리자만 가능하다
    Giftbox giftbox = giftboxRepository.findById(inquiry.getGiftboxIdx());
    if (!giftboxRepository.isManagerOfGiftbox(userIdx, giftbox.getIdx())) {
      throw new BaseExceptionV2(NOT_MANAGER_OF_GIFTBOX);  // 해당 선물 바구니의 관리자만 물어보기 가능함
    }
    inquiryService.completeInquiry(inquiryIdx);
    return ResponseEntity.ok().build();
  }

}