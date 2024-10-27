package clov3r.api.domain.DTO;

import clov3r.api.domain.data.status.LikeStatus;
import clov3r.api.domain.data.status.ProductStatus;
import clov3r.api.domain.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.sql.In;

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
    private int likeCount;
    private ProductStatus status;
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