package clov3r.api.product.repository;

import clov3r.api.product.domain.status.LikeStatus;
import clov3r.domain.domains.entity.Product;
import clov3r.domain.domains.entity.ProductLike;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {

  @Query("SELECT pl FROM ProductLike pl WHERE pl.product.idx = :productIdx AND pl.user.idx = :userIdx")
  ProductLike findProductLike(Long productIdx, Long userIdx);

  @Query("SELECT COUNT(pl) FROM ProductLike pl WHERE pl.product.idx = :productIdx AND pl.likeStatus = :likeStatus")
  int countByProductIdxAndLikeStatus(Long productIdx, LikeStatus likeStatus);

  @Query("SELECT p FROM Product p JOIN ProductLike pl ON p.idx = pl.product.idx WHERE pl.user.idx = :userIdx AND pl.likeStatus = :likeStatus")
  List<Product> findLikeProductList(Long userIdx, LikeStatus likeStatus);

}
