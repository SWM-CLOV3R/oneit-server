package clov3r.api.product.domain.dto;

import clov3r.api.product.domain.status.LikeStatus;
import clov3r.domain.domains.entity.Category;
import clov3r.domain.domains.entity.Product;
import clov3r.domain.domains.type.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO extends ProductSummaryDTO {
    private String brandName;
    private String mallName;
    private String productUrl;
    private Category category;
    private Gender gender;

    public ProductDTO(Product product, LikeStatus likeStatus) {
        super(product, likeStatus);
        this.mallName = product.getMallName();
        this.productUrl = product.getProductUrl();
        this.category = product.getCategory();
        this.gender = product.getGender();
    }
}
