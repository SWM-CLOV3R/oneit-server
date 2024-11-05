package clov3r.api.giftbox.repository.Giftbox;
import clov3r.api.giftbox.domain.request.PostGiftboxRequest;
import clov3r.domain.domains.entity.Giftbox;
import clov3r.domain.domains.entity.GiftboxProduct;
import clov3r.domain.domains.entity.GiftboxUser;
import clov3r.domain.domains.entity.Product;
import java.util.List;

public interface GiftboxCustomRepository {

  Giftbox saveGiftbox(Giftbox giftbox);
  void updateImageUrl(Long idx, String imageUrl);
  Giftbox findByIdx(Long giftboxIdx);
  void deleteByIdx(Long giftboxIdx);
  void updateGiftbox(Long giftboxIdx, PostGiftboxRequest request);
  void addProductToGiftbox(Long giftboxIdx, Long productIdx);
  List<Giftbox> findGiftboxOfUser(Long userIdx);
  List<Product> findProductOfGiftbox(Long giftboxIdx);
  void deleteProductOfGiftbox(Long giftboxIdx, Long productIdx);
  boolean existsByIdx(Long giftboxIdx);
  boolean existProductInGiftbox(Long giftboxIdx, Long productIdx);
  boolean isManagerOfGiftbox(Long userIdx, Long giftboxIdx);
  boolean isParticipantOfGiftbox(Long userIdx, Long giftboxIdx);
  GiftboxUser findGiftboxByInvitationIdx(Long invitationIdx);
  void acceptInvitationToGiftBox(Long userIdx, Long invitationIdx);
  List<GiftboxUser> findParticipantsOfGiftbox(Long giftboxIdx);
  boolean existParticipantOfGiftbox(Long userIdx, Long idx);
  boolean existsInvitationOfGiftbox(Long invitationIdx);
  List<GiftboxProduct> findGiftboxProductList(Long giftboxIdx);
  Giftbox findByInquiryIdx(Long inquiryIdx);
  List<GiftboxProduct> searchProductInGiftbox(String searchKeyword, Long giftboxIdx);
}
