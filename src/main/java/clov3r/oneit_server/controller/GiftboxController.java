package clov3r.oneit_server.controller;

import clov3r.oneit_server.config.security.Auth;
import clov3r.oneit_server.domain.DTO.GiftboxDTO;
import clov3r.oneit_server.domain.data.PostGiftboxRequest;
import clov3r.oneit_server.repository.GiftboxRepository;
import clov3r.oneit_server.response.BaseResponse;
import clov3r.oneit_server.response.BaseResponseStatus;
import clov3r.oneit_server.response.exception.BaseException;
import clov3r.oneit_server.service.GiftboxService;
import clov3r.oneit_server.service.S3Service;
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
    @PostMapping(value="/api/v1/giftbox",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<Long> createGiftbox(@RequestPart("request") PostGiftboxRequest request, @RequestPart(value = "image", required = false) MultipartFile image) {
        // request validation
        if (request.getName() == null || request.getDeadline() == null || request.getCreatedUserIdx() == null || request.getAccessStatus() == null) {
            return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR);
        }
        Long idx = giftboxService.createGiftbox(request);
        String imageUrl = s3Service.upload(image, "giftbox-profile");
        giftboxService.updateGiftboxImageUrl(idx, imageUrl);
        return new BaseResponse<>(idx);
    }

    @Tag(name = "Giftbox API", description = "Giftbox API 목록")
    @GetMapping("/api/v1/giftbox/{giftboxIdx}")
    public BaseResponse<GiftboxDTO> getGiftboxByIdx(@PathVariable("giftboxIdx") Long giftboxIdx) {
        GiftboxDTO giftboxDTO = giftboxRepository.findById(giftboxIdx);
        return new BaseResponse<>(giftboxDTO);
    }
}
