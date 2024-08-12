package clov3r.oneit_server.service;

import static clov3r.oneit_server.response.BaseResponseStatus.DATABASE_ERROR;
import static clov3r.oneit_server.response.BaseResponseStatus.FAIL_TO_UPDATE_GIFTBOX;
import static clov3r.oneit_server.response.BaseResponseStatus.FAIL_TO_UPDATE_GIFTBOX_IMAGE_URL;

import clov3r.oneit_server.domain.data.status.Status;
import clov3r.oneit_server.domain.request.PostGiftboxRequest;
import clov3r.oneit_server.domain.entity.Giftbox;
import clov3r.oneit_server.repository.GiftboxRepository;
import clov3r.oneit_server.repository.ProductRepository;
import clov3r.oneit_server.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GiftboxService {

  private final GiftboxRepository giftboxRepository;
  private final ProductRepository productRepository;

  public Long createGiftbox(PostGiftboxRequest request, Long userIdx) {

    Giftbox newGiftbox = new Giftbox(
        request.getName(),
        request.getDescription(),
        request.getDeadline(),
        userIdx,
        request.getAccessStatus(),
        Status.ACTIVE
    );
    Giftbox saveGiftbox = giftboxRepository.save(newGiftbox);

    // 생성한 유저 idx와 선물 바구니 idx를 연결
    try {
      giftboxRepository.createGiftboxManager(userIdx, saveGiftbox.getIdx());
    } catch (BaseException exception) {
      throw exception;
    }

    // 방금 저장한 giftbox의 idx를 가져옴
    return saveGiftbox.getIdx();
  }

  public void updateGiftboxImageUrl(Long giftboxIdx, String imageUrl) {
    try {
      giftboxRepository.updateImageUrl(giftboxIdx, imageUrl);
    } catch (BaseException exception) {
      throw new BaseException(FAIL_TO_UPDATE_GIFTBOX_IMAGE_URL);
    }
  }

  public void updateGiftbox(Long giftboxIdx, PostGiftboxRequest request) {
    try {
      giftboxRepository.updateGiftbox(giftboxIdx, request);
    } catch (BaseException exception) {
      throw new BaseException(FAIL_TO_UPDATE_GIFTBOX);
    }
  }

  public void addProductToGiftbox(Long giftboxIdx, Long productIdx) {
    try {
      if (!giftboxRepository.existProductInGiftbox(giftboxIdx, productIdx)) {
        giftboxRepository.addProductToGiftbox(giftboxIdx, productIdx);
      }
    } catch (BaseException exception) {
      throw new BaseException(DATABASE_ERROR);
    }
  }
}
