package clov3r.oneit_server.controller;

import clov3r.oneit_server.domain.data.PostGiftboxRequest;
import clov3r.oneit_server.response.BaseResponse;
import clov3r.oneit_server.response.BaseResponseStatus;
import clov3r.oneit_server.service.GiftboxService;
import clov3r.oneit_server.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class GiftboxController {

    private final GiftboxService giftboxService;
    private final S3Service s3Service;

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
}
