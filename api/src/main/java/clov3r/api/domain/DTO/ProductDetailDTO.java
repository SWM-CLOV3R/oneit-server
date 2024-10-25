package clov3r.api.domain.DTO;

import clov3r.api.domain.data.status.LikeStatus;
import clov3r.api.domain.data.status.ProductStatus;
import clov3r.api.domain.entity.Category;
import clov3r.api.domain.entity.Keyword;
import clov3r.api.domain.entity.Product;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class ProductDetailDTO {
    private Long idx;
    private String name;
    private String description;

    private Integer originalPrice;
//    private Integer currentPrice;
//    private Integer discountRate;

    private String brandName;
    private String mallName;
    private String productUrl;
    private String thumbnailUrl;

    private String categoryName;
    private String categoryDisplayName;

    private List<String> keywords = new ArrayList<>();
    private ProductStatus status;
    private int likeCount;
    private LikeStatus likeStatus;

    // 상세 조회시 사용
    public ProductDetailDTO(Product product, List<Keyword> keywords, Category category, LikeStatus likeStatus) {
        this.idx = product.getIdx();
        this.name = product.getName();
        this.description = product.getDescription();
        this.originalPrice = product.getOriginalPrice();
//        this.currentPrice = product.getCurrentPrice();
//        this.discountRate = product.getDiscountRate();
        this.brandName = product.getBrandName();
        this.mallName = product.getMallName();
        this.productUrl = product.getProductUrl();
        this.thumbnailUrl = product.getThumbnailUrl();
        this.categoryName = category.getName();
        this.categoryDisplayName = product.getCategoryDisplayName();
        this.keywords.addAll(keywords.stream().map(Keyword::getName).toList());
        this.status = product.getStatus();
        this.likeCount = product.getLikeCount();
        this.likeStatus = likeStatus;
    }
}