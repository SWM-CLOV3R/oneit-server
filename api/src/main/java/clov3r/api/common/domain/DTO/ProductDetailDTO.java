package clov3r.api.common.domain.DTO;

import clov3r.api.common.domain.entity.Category;
import clov3r.api.common.domain.entity.Keyword;
import clov3r.api.common.domain.entity.Product;
import clov3r.api.common.domain.data.status.LikeStatus;
import clov3r.api.giftbox.domain.status.ProductStatus;
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
    private List<String> keywords = new ArrayList<>();

    // 상세 조회시 사용
    public ProductDetailDTO(Product product, Category category, LikeStatus likeStatus) {
        super(product, likeStatus);
        this.brandName = product.getBrandName();
        this.mallName = product.getMallName();
        this.productUrl = product.getProductUrl();
        this.categoryName = category.getName();
        this.categoryDisplayName = product.getCategoryDisplayName();
        this.keywords.addAll(product.getKeyword().stream().map(Keyword::getName).toList());
    }

}