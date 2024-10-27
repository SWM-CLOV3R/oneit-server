package clov3r.api.repository;

import clov3r.api.domain.data.status.LikeStatus;
import clov3r.api.domain.entity.Product;
import clov3r.api.domain.entity.ProductLike;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {

  @Query("SELECT pl FROM ProductLike pl WHERE pl.productIdx = :productIdx AND pl.userIdx = :userIdx")
  ProductLike findProductLike(Long productIdx, Long userIdx);

  @Query("SELECT COUNT(pl) FROM ProductLike pl WHERE pl.productIdx = :productIdx AND pl.likeStatus = :likeStatus")
  int countByProductIdxAndLikeStatus(Long productIdx, LikeStatus likeStatus);

  @Query("SELECT p FROM Product p JOIN ProductLike pl ON p.idx = pl.productIdx WHERE pl.userIdx = :userIdx AND pl.likeStatus = :likeStatus")
  List<Product> findLikeProductList(Long userIdx, LikeStatus likeStatus);

}
