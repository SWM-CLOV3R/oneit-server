package clov3r.api.controller;

import static clov3r.api.error.errorcode.CommonErrorCode.REQUEST_ERROR;
import static clov3r.api.error.errorcode.CustomErrorCode.ALREADY_USED_INQUIRY;
import static clov3r.api.error.errorcode.CustomErrorCode.GIFTBOX_NOT_FOUND;
import static clov3r.api.error.errorcode.CustomErrorCode.NOT_EXIST_EMOJI;
import static clov3r.api.error.errorcode.CustomErrorCode.NOT_EXIST_INQUIRY;
import static clov3r.api.error.errorcode.CustomErrorCode.NOT_EXIST_INQUIRY_PRODUCT;
import static clov3r.api.error.errorcode.CustomErrorCode.NOT_MANAGER_OF_GIFTBOX;
import static clov3r.api.error.errorcode.CustomErrorCode.PRODUCT_NOT_FOUND;

import clov3r.api.config.security.Auth;
import clov3r.api.domain.DTO.InquiryDTO;
import clov3r.api.domain.DTO.ProductSummaryDTO;
import clov3r.api.domain.data.ProductEmoji;
import clov3r.api.domain.data.status.InquiryStatus;
import clov3r.api.domain.entity.Inquiry;
import clov3r.api.domain.entity.Product;
import clov3r.api.domain.request.InquiryRequest;
import clov3r.api.error.exception.BaseExceptionV2;
import clov3r.api.repository.GiftboxProductRepository;
import clov3r.api.repository.GiftboxRepository;
import clov3r.api.repository.InquiryProductRepository;
import clov3r.api.repository.InquiryRepository;
import clov3r.api.repository.ProductRepository;
import clov3r.api.service.GiftboxProductService;
import clov3r.api.service.InquiryProductService;
import clov3r.api.service.InquiryService;
import clov3r.api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
  private final InquiryProductService inquiryProductService;
  private final InquiryProductRepository inquiryProductRepository;
  private final ProductRepository productRepository;
  private final ProductService productService;
  private final GiftboxProductService giftboxProductService;
  private final GiftboxProductRepository giftboxProductRepository;

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
    // product and giftbox validation : 선물바구니 내부 제품으로만 물어보기 생성 가능
    List<Product> productList = giftboxRepository.findProductOfGiftbox(inquiryRequest.getGiftboxIdx());
    for (Long productIdx : inquiryRequest.getProductIdxList()) {
      if (productList.stream().noneMatch(product -> product.getIdx().equals(productIdx))) {
        throw new BaseExceptionV2(PRODUCT_NOT_FOUND);
      }
    }

    // ================================
    // make inquiry
    Long idx = inquiryService.createInquiry(inquiryRequest, userIdx);
    inquiryService.createInquiryProduct(idx, inquiryRequest.getProductIdxList(), inquiryRequest.getGiftboxIdx());
    return ResponseEntity.ok(idx);
  }

  @Tag(name = "물어보기 API", description = "물어보기 API 목록")
  @Operation(summary = "물어보기 조회", description = "물어보기 내용을 조회합니다.(물어보기 idx 기준)")
  @GetMapping("/api/v2/inquiry/{inquiryIdx}")
  public ResponseEntity<InquiryDTO> getInquiry(
      @PathVariable("inquiryIdx") Long inquiryIdx,
      @Parameter(hidden = true) @Auth(required = false) Long userIdx
  ) {

    Inquiry inquiry = inquiryRepository.findByIdx(inquiryIdx);
    if (inquiry == null) {
      throw new BaseExceptionV2(NOT_EXIST_INQUIRY);
    }
    if (inquiry.getInquiryStatus().equals(InquiryStatus.COMPLETE)) {
      throw new BaseExceptionV2(ALREADY_USED_INQUIRY);
    }
    List<Product> productList = inquiryProductRepository.findProductListByInquiry(inquiry);
    InquiryDTO inquiryDTO = new InquiryDTO(inquiry,
        productList.stream().map(
            product -> new ProductSummaryDTO(
                product,
                productService.getLikeStatus(product.getIdx(), userIdx)
            )
        ).toList());

    return ResponseEntity.ok(inquiryDTO);
  }

  @Tag(name = "물어보기 API", description = "물어보기 API 목록")
  @Operation(summary = "물어보기 이모지 추가", description = "물어보기 제품에 대해 이모지를 추가합니다. 계정 정보는 받지 않습니다.")
  @PostMapping("/api/v2/inquiry/{inquiryIdx}/emoji")
  public ResponseEntity<Long> addEmoji(
      @PathVariable("inquiryIdx") Long inquiryIdx,
      @RequestBody List<ProductEmoji> productEmojiList
  ) {
    if (productEmojiList.isEmpty()) {
      throw new BaseExceptionV2(REQUEST_ERROR);
    }
    if (productEmojiList.stream().anyMatch(
        productEmoji -> productRepository.findById(productEmoji.getProductIdx()) == null)) {
      throw new BaseExceptionV2(PRODUCT_NOT_FOUND);
    }
    Inquiry inquiry = inquiryRepository.findByIdx(inquiryIdx);
    if (inquiry == null) {
      throw new BaseExceptionV2(NOT_EXIST_INQUIRY);
    }
    if (inquiry.getInquiryStatus().equals(InquiryStatus.COMPLETE)) {
      throw new BaseExceptionV2(ALREADY_USED_INQUIRY);
    }
    if (productEmojiList.stream().anyMatch(
        productEmoji -> !inquiryRepository.existInquiryProduct(inquiryIdx,
            productEmoji.getProductIdx()))) {
      throw new BaseExceptionV2(NOT_EXIST_INQUIRY_PRODUCT);
    }
    if (productEmojiList.stream().anyMatch(
        productEmoji -> !giftboxProductRepository.existProductByGiftbox(
            inquiry.getGiftbox().getIdx(),
            productEmoji.getProductIdx()))) {
      throw new BaseExceptionV2(PRODUCT_NOT_FOUND);
    }

    inquiryProductService.addEmoji(inquiryIdx, productEmojiList); // add emoji to inquiry product
    giftboxProductService.addEmojiToGiftbox(inquiry.getGiftbox().getIdx(), productEmojiList);  //  add emoji to giftbox inquiry result
    inquiryService.completeInquiry(inquiryIdx);
    return ResponseEntity.ok(inquiryIdx);
  }

}