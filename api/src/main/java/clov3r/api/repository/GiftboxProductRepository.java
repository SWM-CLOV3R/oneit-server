package clov3r.api.repository;

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

}
