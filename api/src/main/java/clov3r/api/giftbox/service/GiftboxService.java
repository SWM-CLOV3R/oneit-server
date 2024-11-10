package clov3r.api.giftbox.service;

import static clov3r.api.common.error.errorcode.CommonErrorCode.DATABASE_ERROR;
import static clov3r.api.common.error.errorcode.CustomErrorCode.FAIL_TO_UPDATE_GIFTBOX;
import static clov3r.api.common.error.errorcode.CustomErrorCode.FAIL_TO_UPDATE_GIFTBOX_IMAGE_URL;

import clov3r.api.giftbox.repository.Giftbox.GiftboxRepository;
import clov3r.api.product.repository.KeywordRepository;
import clov3r.api.giftbox.domain.dto.GiftboxProductDTO;
import clov3r.api.giftbox.domain.request.PostGiftboxRequest;
import clov3r.api.common.error.exception.BaseExceptionV2;
import clov3r.api.giftbox.repository.GiftboxUser.GiftboxUserRepository;
import clov3r.api.auth.repository.UserRepository;
import clov3r.api.notification.service.NotificationService;
import clov3r.domain.domains.entity.Giftbox;
import clov3r.domain.domains.entity.GiftboxProduct;
import clov3r.domain.domains.entity.GiftboxUser;
import clov3r.domain.domains.status.InvitationStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GiftboxService {

  private final GiftboxRepository giftboxRepository;
  private final GiftboxUserRepository giftboxUserRepository;
  private final UserRepository userRepository;
  private final NotificationService notificationService;
  private final GiftboxProductService giftboxProductService;
  private final KeywordRepository keywordRepository;

  public Long createGiftbox(PostGiftboxRequest request, Long userIdx) {

    Giftbox newGiftbox = new Giftbox(
        request.getName(),
        request.getDeadline(),
        userIdx
    );
    Giftbox saveGiftbox = giftboxRepository.saveGiftbox(newGiftbox);

    // 생성한 유저 idx와 선물 바구니 idx를 연결
    createGiftboxManager(userIdx, saveGiftbox.getIdx());

    // 방금 저장한 giftbox의 idx를 가져옴
    return saveGiftbox.getIdx();
  }

  void createGiftboxManager(Long userIdx, Long giftboxIdx) {
    GiftboxUser giftboxUser = new GiftboxUser();
    giftboxUser.createManager(
        giftboxRepository.findByIdx(giftboxIdx),
        userRepository.findByUserIdx(userIdx)
    );
    giftboxUserRepository.save(giftboxUser);
  }

  public void updateGiftboxImageUrl(Long giftboxIdx, String imageUrl) {
    try {
      giftboxRepository.updateImageUrl(giftboxIdx, imageUrl);
    } catch (BaseExceptionV2 exception) {
      throw new BaseExceptionV2(FAIL_TO_UPDATE_GIFTBOX_IMAGE_URL);
    }
  }

  public void updateGiftbox(Long giftboxIdx, PostGiftboxRequest request) {
    try {
      giftboxRepository.updateGiftbox(giftboxIdx, request);
    } catch (BaseExceptionV2 exception) {
      throw new BaseExceptionV2(FAIL_TO_UPDATE_GIFTBOX);
    }
  }

  public void addProductToGiftbox(Long giftboxIdx, Long productIdx) {
    try {
      if (!giftboxRepository.existProductInGiftbox(giftboxIdx, productIdx)) {
        giftboxRepository.addProductToGiftbox(giftboxIdx, productIdx);
      }
    } catch (BaseExceptionV2 exception) {
      throw new BaseExceptionV2(DATABASE_ERROR);
    }
  }

  // 선물바구니 초대장 생성 (userIdx가 null, invitationStatus가 PENDING)
  public Long inviteUserToGiftBox(Long giftboxIdx, Long userIdx) {
    // giftboxIdx로 status가 ACTIVE인 giftboxUser를 생성하고 invitationStatus를 PENDING으로 설정
    GiftboxUser giftboxUser = new GiftboxUser();
    giftboxUser.createInvitation(
        giftboxRepository.findByIdx(giftboxIdx),
        userRepository.findByUserIdx(userIdx)
    );
    giftboxUserRepository.save(giftboxUser);
    return giftboxUser.getIdx();
  }

  // 선물바구니 초대 수락
  public void acceptInvitationToGiftBox(GiftboxUser giftboxUser, Long userIdx, Long invitationIdx) {
    if (giftboxUser.getInvitationStatus().equals(InvitationStatus.PENDING) && giftboxUser.getUser() == null) {
      // accept invitation to giftbox
      giftboxRepository.acceptInvitationToGiftBox(userIdx, invitationIdx);
    }
    else {
      // 새로운 참여자 row 추가
      GiftboxUser newGiftboxUser = new GiftboxUser();
      newGiftboxUser.createAcceptInvitation(
          giftboxUser,
          userRepository.findByUserIdx(userIdx)
      );
      giftboxUserRepository.save(newGiftboxUser);
    }

    // send notification
    notificationService.sendGiftboxInvitationAcceptanceNotification(invitationIdx, giftboxUser.getGiftbox().getIdx(), userIdx);

  }

  public List<GiftboxProductDTO> searchProductInGiftbox(String searchKeyword, Long giftboxIdx, Long userIdx) {
    List<GiftboxProduct> giftboxProducts = giftboxRepository.searchProductInGiftbox(searchKeyword, giftboxIdx);
    return giftboxProducts.stream()
        .map(giftboxProduct -> new GiftboxProductDTO(
            giftboxProduct,
            giftboxProductService.getVoteStatusOfUser(
                userIdx,
                giftboxIdx,
                giftboxProduct.getProduct().getIdx()),
            keywordRepository.findKeywordByProductIdx(giftboxProduct.getProduct().getIdx())
        )).toList();
  }

  public List<GiftboxProductDTO> findGiftboxProductList(Long giftboxIdx, Long userIdx) {
    List<GiftboxProduct> giftboxProductList = giftboxRepository.findGiftboxProductList(giftboxIdx);
    return giftboxProductList.stream()
        .map(giftboxProduct ->
            new GiftboxProductDTO(
                giftboxProduct,
                giftboxProductService.getVoteStatusOfUser(
                    userIdx,
                    giftboxIdx,
                    giftboxProduct.getProduct().getIdx()),
                keywordRepository.findKeywordByProductIdx(giftboxProduct.getProduct().getIdx())
            )).toList();
  }


  public void deleteByIdx(Long giftboxIdx) {
    giftboxRepository.deleteByIdx(giftboxIdx);
  }
}