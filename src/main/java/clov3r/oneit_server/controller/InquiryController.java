package clov3r.oneit_server.controller;

import static clov3r.oneit_server.error.errorcode.CustomErrorCode.GIFTBOX_NOT_FOUND;
import static clov3r.oneit_server.error.errorcode.CustomErrorCode.NOT_MANAGER_OF_GIFTBOX;
import static clov3r.oneit_server.error.errorcode.CustomErrorCode.PRODUCT_NOT_FOUND;

import clov3r.oneit_server.config.security.Auth;
import clov3r.oneit_server.domain.DTO.InquiryDTO;
import clov3r.oneit_server.domain.DTO.ProductSummaryDTO;
import clov3r.oneit_server.domain.entity.Inquiry;
import clov3r.oneit_server.domain.entity.Product;
import clov3r.oneit_server.domain.request.InquiryRequest;
import clov3r.oneit_server.error.exception.BaseExceptionV2;
import clov3r.oneit_server.repository.GiftboxRepository;
import clov3r.oneit_server.repository.InquiryRepository;
import clov3r.oneit_server.repository.ProductRepository;
import clov3r.oneit_server.service.GiftboxService;
import clov3r.oneit_server.service.InquiryService;
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

  private final GiftboxService giftboxService;
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
    InquiryDTO inquiryDTO = new InquiryDTO(inquiry, productList.stream().map(ProductSummaryDTO::new).toList());

    return ResponseEntity.ok(inquiryDTO);
  }

}
