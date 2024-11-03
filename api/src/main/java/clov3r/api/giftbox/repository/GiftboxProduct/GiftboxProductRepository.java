package clov3r.api.giftbox.repository.GiftboxProduct;

import clov3r.api.giftbox.domain.data.EmojiName;
import clov3r.api.giftbox.domain.entity.GiftboxProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftboxProductRepository extends JpaRepository<GiftboxProduct, Long> {

  @Modifying
  @Query("update GiftboxProduct set purchaseStatus = 'PURCHASED' where giftbox.idx = :giftboxIdx and product.idx = :productIdx")
  void purchaseProduct(Long giftboxIdx, Long productIdx);

  @Modifying
  @Query("update GiftboxProduct set emojiName = :emojiName where giftbox.idx = :giftboxIdx and product.idx = :productIdx")
  void updateEmojiToGiftbox(Long giftboxIdx, Long productIdx, EmojiName emojiName);

  @Query("select count(gp) > 0 from GiftboxProduct gp where gp.giftbox.idx = :giftboxIdx and gp.product.idx = :productIdx and gp.status = 'ACTIVE'")
  boolean existProductByGiftbox(Long giftboxIdx, Long productIdx);

  @Query("select gp from GiftboxProduct gp where gp.giftbox.idx = :giftboxIdx and gp.product.idx = :productIdx and gp.status = 'ACTIVE'")
  GiftboxProduct findByGiftboxIdxAndProductIdx(Long giftboxIdx, Long productIdx);

  @Query("select count(p) > 0 from Product p where p.idx = :productIdx and (p.status = 'ACTIVE' or p.status = 'INVALID')")
  boolean validProductInGiftbox(Long productIdx);

}
