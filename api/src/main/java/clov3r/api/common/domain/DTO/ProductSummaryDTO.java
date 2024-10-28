package clov3r.api.common.domain.DTO;

import clov3r.api.common.domain.data.status.LikeStatus;
import clov3r.api.common.domain.entity.Product;
import clov3r.api.giftbox.domain.status.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSummaryDTO {
    private Long idx;
    private String name;
    private Integer originalPrice;
//    private Integer currentPrice;
//    private Integer discountRate;
    private String thumbnailUrl;
    private ProductStatus status;
    private int likeCount;
    private LikeStatus likeStatus;

    // 리스트 조회시 사용
    public ProductSummaryDTO(Product product, LikeStatus likeStatus) {
        this.idx = product.getIdx();
        this.name = product.getName();
        this.originalPrice = product.getOriginalPrice();
//        this.currentPrice = product.getCurrentPrice();
//        this.discountRate = product.getDiscountRate();
        this.thumbnailUrl = product.getThumbnailUrl();
        this.status = product.getStatus();
        this.likeCount = product.getLikeCount();
        this.likeStatus = likeStatus;
    }

    public ProductSummaryDTO(Product product) {
        this.idx = product.getIdx();
        this.name = product.getName();
        this.originalPrice = product.getOriginalPrice();
        this.thumbnailUrl = product.getThumbnailUrl();
        this.status = product.getStatus();
        this.likeCount = product.getLikeCount();
    }
}