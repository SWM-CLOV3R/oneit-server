package clov3r.api.common.domain.DTO;

import clov3r.api.common.domain.data.Gender;
import clov3r.api.common.domain.data.status.LikeStatus;
import clov3r.api.giftbox.domain.status.ProductStatus;
import clov3r.api.common.domain.entity.Category;
import clov3r.api.common.domain.entity.Keyword;
import clov3r.api.common.domain.entity.Product;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ProductDTO {
    private Long idx;
    private String name;
    private String description;
    private Integer originalPrice;
//    private Integer currentPrice;
//    private Integer discountRate;
    private String thumbnailUrl;
    private String mallName;
    private String productUrl;
    private Category category;
    private List<Keyword> keywords  = new ArrayList<>();
    private ProductStatus status;
    private Gender gender;

    private int likeCount;
    private LikeStatus likeStatus;

    public ProductDTO(Product product, List<Keyword> keywords, LikeStatus likeStatus) {
        this.idx = product.getIdx();
        this.name = product.getName();
        this.description = product.getDescription();
        this.originalPrice = product.getOriginalPrice();
//        this.currentPrice = product.getCurrentPrice();
//        this.discountRate = product.getDiscountRate();
        this.thumbnailUrl = product.getThumbnailUrl();
        this.mallName = product.getMallName();
        this.productUrl = product.getProductUrl();
        this.category = product.getCategory();
        this.keywords.addAll(keywords);
        this.gender = product.getGender();
        this.status = product.getStatus();
        this.likeCount = product.getLikeCount();
        this.likeStatus = likeStatus;
    }
}
