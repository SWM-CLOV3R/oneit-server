package clov3r.oneit_server.controller;

import clov3r.oneit_server.config.security.Auth;
import clov3r.oneit_server.domain.DTO.GiftboxDTO;
import clov3r.oneit_server.domain.data.PostGiftboxRequest;
import clov3r.oneit_server.domain.entity.Giftbox;
import clov3r.oneit_server.repository.GiftboxRepository;
import clov3r.oneit_server.response.BaseResponse;
import clov3r.oneit_server.response.BaseResponseStatus;
import clov3r.oneit_server.response.exception.BaseException;
import clov3r.oneit_server.service.GiftboxService;
import clov3r.oneit_server.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class GiftboxController {

  private final GiftboxService giftboxService;
  private final S3Service s3Service;
  private final GiftboxRepository giftboxRepository;

  @Tag(name = "Giftbox API", description = "Giftbox API 목록")
  @Operation(summary = "Giftbox 생성", description = "선물 바구니 생성, 이미지는 선택적으로 업로드 가능, 이미지를 업로드하지 않을 경우 null로 저장")
  @PostMapping(value = "/api/v1/giftbox", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public BaseResponse<Long> createGiftbox(
      @RequestPart("request") PostGiftboxRequest request,
      @RequestPart(value = "image", required = false) MultipartFile image
  ) {
    // request validation
    if (request.getName() == null || request.getDeadline() == null
        || request.getCreatedUserIdx() == null || request.getAccessStatus() == null) {
      return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR);
    }
    Long idx = giftboxService.createGiftbox(request);
    if (image != null) {
      String imageUrl = s3Service.upload(image, "giftbox-profile");
      giftboxService.updateGiftboxImageUrl(idx, imageUrl);
    }
    return new BaseResponse<>(idx);
  }

  @Tag(name = "Giftbox API", description = "Giftbox API 목록")
  @Operation(summary = "Giftbox 조회", description = "선물 바구니의 idx로 선물 바구니 조회, 삭제한 선물 바구니는 조회되지 않음")
  @GetMapping("/api/v1/giftbox/{giftboxIdx}")
  public BaseResponse<GiftboxDTO> getGiftboxByIdx(@PathVariable("giftboxIdx") Long giftboxIdx) {
    try {
      Giftbox giftbox = giftboxRepository.findById(giftboxIdx);
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
      return new BaseResponse<>(BaseResponseStatus.GIFTBOX_NOT_FOUND);
    }

  }

  @Tag(name = "Giftbox API", description = "Giftbox API 목록")
  @Operation(summary = "Giftbox 삭제", description = "선물 바구니의 idx로 선물 바구니 삭제")
  @DeleteMapping("/api/v1/giftbox/{giftboxIdx}")
  public BaseResponse<String> deleteGiftboxByIdx(@PathVariable("giftboxIdx") Long giftboxIdx) {
    if (giftboxRepository.findById(giftboxIdx) == null) {
      return new BaseResponse<>(BaseResponseStatus.GIFTBOX_NOT_FOUND);
    }
    try {
      giftboxRepository.deleteById(giftboxIdx);
      return new BaseResponse<>(giftboxIdx + "번 선물 바구니가 삭제되었습니다.");
    } catch (BaseException e) {
      return new BaseResponse<>(BaseResponseStatus.GIFTBOX_NOT_FOUND);
    }
  }

  @Tag(name = "Giftbox API", description = "Giftbox API 목록")
  @Operation(summary = "Giftbox 수정", description = "선물 바구니의 idx로 선물 바구니 수정, 이미지는 선택적으로 업로드 가능, 이미지를 업로드하지 않을 경우 null로 저장")
  @PutMapping(value = "/api/v1/giftbox/{giftboxIdx}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public BaseResponse<String> updateGiftbox(
      @PathVariable("giftboxIdx") Long giftboxIdx,
      @RequestPart("request") PostGiftboxRequest request,
      @RequestPart(value = "image", required = false) MultipartFile image) {
    // request validation
    if (request.getName() == null || request.getDeadline() == null
        || request.getCreatedUserIdx() == null || request.getAccessStatus() == null) {
      return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR);
    }
    if (giftboxRepository.findById(giftboxIdx) == null) {
      return new BaseResponse<>(BaseResponseStatus.GIFTBOX_NOT_FOUND);
    }
    giftboxService.updateGiftbox(giftboxIdx, request);
    if (image != null) {
      String imageUrl = s3Service.upload(image, "giftbox-profile");
      giftboxService.updateGiftboxImageUrl(giftboxIdx, imageUrl);
    }
    return new BaseResponse<>(giftboxIdx + "번 선물 바구니가 수정되었습니다.");
  }
}
