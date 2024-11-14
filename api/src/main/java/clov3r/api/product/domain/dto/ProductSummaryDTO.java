package clov3r.api.product.domain.dto;

import clov3r.api.product.domain.status.LikeStatus;
import clov3r.api.product.domain.status.ProductStatus;
import clov3r.domain.domains.entity.Keyword;
import clov3r.domain.domains.entity.Product;
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
    private List<Keyword> keywords = new ArrayList<>();
    private List<String> displayTags = new ArrayList<>();

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
        this.keywords = product.getKeyword();
        this.displayTags = product.getDisplayTags();

        this.likeCount = product.getLikeCount();
        this.likeStatus = likeStatus;
    }

}