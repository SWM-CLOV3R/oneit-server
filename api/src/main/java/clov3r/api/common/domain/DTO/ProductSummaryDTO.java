package clov3r.api.common.domain.DTO;

import clov3r.api.common.domain.data.status.LikeStatus;
import clov3r.api.common.domain.entity.Keyword;
import clov3r.api.common.domain.entity.Product;
import clov3r.api.giftbox.domain.status.ProductStatus;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSummaryDTO {

    private Long idx;
    private String name;
    private String description;
    private Integer originalPrice;
    //    private Integer currentPrice;
//    private Integer discountRate;
    private String thumbnailUrl;
    private ProductStatus productStatus;
    private int likeCount;
    private LikeStatus likeStatus;

    // 리스트 조회시 사용
    public ProductSummaryDTO(Product product, LikeStatus likeStatus) {
        this.idx = product.getIdx();
        this.name = product.getName();
        this.description = product.getDescription();
        this.originalPrice = product.getOriginalPrice();
//        this.currentPrice = product.getCurrentPrice();
//        this.discountRate = product.getDiscountRate();
        this.thumbnailUrl = product.getThumbnailUrl();
        this.productStatus = product.getStatus();
        this.likeCount = product.getLikeCount();
        this.likeStatus = likeStatus;
    }

}