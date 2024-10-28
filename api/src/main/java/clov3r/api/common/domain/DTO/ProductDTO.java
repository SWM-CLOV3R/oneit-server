package clov3r.api.common.domain.DTO;

import clov3r.api.common.domain.data.Gender;
import clov3r.api.common.domain.data.status.LikeStatus;
import clov3r.api.common.domain.entity.Category;
import clov3r.api.common.domain.entity.Keyword;
import clov3r.api.common.domain.entity.Product;
import java.util.ArrayList;
import java.util.List;
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
    private List<Keyword> keywords = new ArrayList<>();

    public ProductDTO(Product product, LikeStatus likeStatus) {
        super(product, likeStatus);
        this.mallName = product.getMallName();
        this.productUrl = product.getProductUrl();
        this.category = product.getCategory();
        this.gender = product.getGender();
        this.keywords.addAll(product.getKeyword());
    }
}
