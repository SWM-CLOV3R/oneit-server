package clov3r.api.product.domain.dto;

import clov3r.api.product.domain.entity.Category;
import clov3r.api.product.domain.entity.Keyword;
import clov3r.api.product.domain.entity.Product;
import clov3r.api.product.domain.status.LikeStatus;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductDetailDTO extends ProductSummaryDTO {

    private String brandName;
    private String mallName;
    private String productUrl;
    private String categoryName;
    private String categoryDisplayName;
    private Category category;


    // 상세 조회시 사용
    public ProductDetailDTO(Product product, LikeStatus likeStatus) {
        super(product, likeStatus);
        this.brandName = product.getBrandName();
        this.mallName = product.getMallName();
        this.productUrl = product.getProductUrl();
        this.category = product.getCategory();
        this.categoryName = product.getCategory().getName();
        this.categoryDisplayName = product.getCategoryDisplayName();
    }

}