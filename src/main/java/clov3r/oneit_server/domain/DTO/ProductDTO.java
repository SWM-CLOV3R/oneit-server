package clov3r.oneit_server.domain.DTO;

import clov3r.oneit_server.domain.data.Gender;
import clov3r.oneit_server.domain.entity.Keyword;
import clov3r.oneit_server.domain.entity.Product;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDTO {
    private Long idx;
    private String name;
    private int originalPrice;
    private String shoppingmall;
    private String productUrl;
    private String thumbnailUrl;
    private Long categoryIdx;
    private List<Keyword> keywords  = new ArrayList<>();
    private Gender gender;

    public ProductDTO(Product product, List<Keyword> keywords) {
        this.idx = product.getIdx();
        this.name = product.getName();
        this.originalPrice = product.getOriginalPrice();
        this.shoppingmall = product.getMallName();
        this.productUrl = product.getProductUrl();
        this.thumbnailUrl = product.getThumbnailUrl();
        this.keywords.addAll(keywords);
        this.gender = product.getGender();
    }
}
