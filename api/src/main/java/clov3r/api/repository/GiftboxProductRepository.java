package clov3r.api.repository;

import clov3r.api.domain.data.EmojiName;
import clov3r.api.domain.data.ProductEmoji;
import clov3r.api.domain.entity.GiftboxProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftboxProductRepository extends JpaRepository<GiftboxProduct, Long> {

  @Query("select idx from GiftboxProduct where giftbox.idx = :giftboxIdx and product.idx = :productIdx and status = 'ACTIVE'")
  Long findGiftboxProductIdxByGiftboxIdxAndProductIdx(Long giftboxIdx, Long productIdx);

  @Modifying
  @Query("update GiftboxProduct set purchaseStatus = 'PURCHASED' where giftbox.idx = :giftboxIdx and product.idx = :productIdx")
  void purchaseProduct(Long giftboxIdx, Long productIdx);

  @Modifying
  @Query("update GiftboxProduct set emojiName = :emojiName where giftbox.idx = :giftboxIdx and product.idx = :productIdx")
  void updateEmojiToGiftbox(Long giftboxIdx, Long productIdx, EmojiName emojiName);

  @Query("select count(gp) > 0 from GiftboxProduct gp where gp.giftbox.idx = :giftboxIdx and gp.product.idx = :productIdx and gp.status = 'ACTIVE'")
  boolean existProductByGiftbox(Long giftboxIdx, Long productIdx);

}
