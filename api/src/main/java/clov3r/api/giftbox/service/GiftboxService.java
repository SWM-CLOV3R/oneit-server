package clov3r.api.giftbox.service;

import static clov3r.api.common.error.errorcode.CommonErrorCode.DATABASE_ERROR;
import static clov3r.api.common.error.errorcode.CustomErrorCode.FAIL_TO_UPDATE_GIFTBOX;
import static clov3r.api.common.error.errorcode.CustomErrorCode.FAIL_TO_UPDATE_GIFTBOX_IMAGE_URL;

import clov3r.api.giftbox.domain.dto.GiftboxProductDTO;
import clov3r.api.common.domain.DTO.ProductSummaryDTO;
import clov3r.api.giftbox.domain.data.GiftboxUserRole;
import clov3r.api.common.domain.data.status.InvitationStatus;
import clov3r.api.common.domain.data.status.Status;
import clov3r.api.giftbox.domain.entity.Giftbox;
import clov3r.api.giftbox.domain.entity.GiftboxProduct;
import clov3r.api.giftbox.domain.entity.GiftboxUser;
import clov3r.api.common.domain.entity.Product;
import clov3r.api.giftbox.domain.request.PostGiftboxRequest;
import clov3r.api.common.error.exception.BaseExceptionV2;
import clov3r.api.giftbox.repository.GiftboxRepository;
import clov3r.api.giftbox.repository.GiftboxUserRepository;
import clov3r.api.giftbox.repository.InquiryRepository;
import clov3r.api.common.repository.NotificationRepository;
import clov3r.api.common.repository.UserRepository;
import clov3r.api.common.service.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class GiftboxService {

  private final GiftboxRepository giftboxRepository;
  private final GiftboxUserRepository giftboxUserRepository;
  private final UserRepository userRepository;
  private final NotificationRepository notificationRepository;
  private final NotificationService notificationService;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final GiftboxProductService giftboxProductService;
  private final InquiryRepository inquiryRepository;

  public Long createGiftbox(PostGiftboxRequest request, Long userIdx) {

    Giftbox newGiftbox = new Giftbox(
        request.getName(),
        request.getDeadline(),
        userIdx,
        Status.ACTIVE
    );
    Giftbox saveGiftbox = giftboxRepository.save(newGiftbox);

    // 생성한 유저 idx와 선물 바구니 idx를 연결
    try {
      giftboxRepository.createGiftboxManager(userIdx, saveGiftbox.getIdx());
    } catch (BaseExceptionV2 exception) {
      throw exception;
    }

    // 방금 저장한 giftbox의 idx를 가져옴
    return saveGiftbox.getIdx();
  }

  public void updateGiftboxImageUrl(Long giftboxIdx, String imageUrl) {
    try {
      giftboxRepository.updateImageUrl(giftboxIdx, imageUrl);
    } catch (BaseExceptionV2 exception) {
      throw new BaseExceptionV2(FAIL_TO_UPDATE_GIFTBOX_IMAGE_URL);
    }
  }

  @Transactional
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

  public Long inviteUserToGiftBox(Long giftboxIdx, Long userIdx) {
    // giftboxIdx로 status가 ACTIVE인 giftboxUser를 생성하고 invitationStatus를 PENDING으로 설정
    GiftboxUser giftboxUser = GiftboxUser.builder()
        .giftbox(giftboxRepository.findById(giftboxIdx))
        .sender(userRepository.findByUserIdx(userIdx))
        .userRole(GiftboxUserRole.PARTICIPANT)
        .invitationStatus(InvitationStatus.PENDING)
        .build();
    giftboxUser.createBaseEntity();
    giftboxUserRepository.save(giftboxUser);
    return giftboxUser.getIdx();
  }

  public void acceptInvitationToGiftBox(GiftboxUser giftboxUser, Long userIdx, Long invitationIdx) {
    giftboxRepository.acceptInvitationToGiftBox(userIdx, invitationIdx);

    // send notification
    notificationService.sendGiftboxInvitationAcceptanceNotification(invitationIdx, giftboxUser.getGiftbox().getIdx(), userIdx);

  }

  public List<ProductSummaryDTO> searchProductInGiftbox(String searchKeyword, Long giftboxIdx) {
    List<Product> products = giftboxRepository.searchProductInGiftbox(searchKeyword, giftboxIdx);
    return products.stream()
        .map(ProductSummaryDTO::new).toList();
  }

  public List<GiftboxProductDTO> findGiftboxProductList(Long giftboxIdx, Long userIdx) {
    List<GiftboxProduct> giftboxProductList = giftboxRepository.findGiftboxProductList(giftboxIdx);
    return giftboxProductList.stream()
        .map(giftboxProduct ->
            new GiftboxProductDTO(
                giftboxProduct,
                giftboxProductService.getVoteStatusOfUser(
                    userIdx,
                    giftboxProduct.getGiftbox().getIdx(),
                    giftboxProduct.getProduct().getIdx())
            )).toList();
  }
}