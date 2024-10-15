package clov3r.api.domain.DTO;

import clov3r.api.domain.data.Gender;
import clov3r.api.domain.entity.Category;
import clov3r.api.domain.entity.Keyword;
import clov3r.api.domain.entity.Product;
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
    private int originalPrice;
    private int currentPrice;
    private int discountRate;
    private String thumbnailUrl;
    private String mallName;
    private String productUrl;
    private Category category;
    private List<Keyword> keywords  = new ArrayList<>();
    private Gender gender;

    public ProductDTO(Product product, List<Keyword> keywords) {
        this.idx = product.getIdx();
        this.name = product.getName();
        this.description = product.getDescription();
        this.originalPrice = product.getOriginalPrice();
        this.currentPrice = product.getCurrentPrice();
        this.discountRate = product.getDiscountRate();
        this.thumbnailUrl = product.getThumbnailUrl();
        this.mallName = product.getMallName();
        this.productUrl = product.getProductUrl();
        this.category = product.getCategory();
        this.keywords.addAll(keywords);
        this.gender = product.getGender();
    }
}
