package clov3r.api.product.domain.dto;

import clov3r.domain.domains.entity.Category;
import clov3r.domain.domains.entity.Product;
import clov3r.domain.domains.status.LikeStatus;
import com.fasterxml.jackson.databind.JsonNode;
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
    private List<String> detailImages = new ArrayList<>();
    private JsonNode options;


    // 상세 조회시 사용
    public ProductDetailDTO(Product product, LikeStatus likeStatus, List<String> detailImages) {
        super(product, likeStatus);
        this.brandName = product.getBrandName();
        this.mallName = product.getMallName();
        this.productUrl = product.getProductUrl();
        this.category = product.getCategory();
        this.categoryName = product.getCategory().getName();
        this.categoryDisplayName = product.getCategoryDisplayName();
        this.detailImages = detailImages;
        this.options = product.getOptions();
    }

}