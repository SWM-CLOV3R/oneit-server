package clov3r.oneit_server.domain.DTO;

import clov3r.oneit_server.domain.entity.Category;
import clov3r.oneit_server.domain.entity.Keyword;
import clov3r.oneit_server.domain.entity.Product;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ProductDetailDTO {
    private Long idx;
    private String name;
    private int originalPrice;
    private int currentPrice;
    private int discountRate;

    private String brandName;
    private String mallName;
    private String productUrl;
    private String thumbnailUrl;

    private String categoryName;
    private String categoryDisplayName;

    private List<String> keywords = new ArrayList<>();

    private String status;

    public ProductDetailDTO(Product product, List<Keyword> keywords, Category category) {
        this.idx = product.getIdx();
        this.name = product.getName();
        this.originalPrice = product.getOriginalPrice();
        this.currentPrice = product.getCurrentPrice();
        this.discountRate = product.getDiscountRate();
        this.brandName = product.getBrandName();
        this.mallName = product.getMallName();
        this.productUrl = product.getProductUrl();
        this.thumbnailUrl = product.getThumbnailUrl();
        this.categoryName = category.getName();
        this.categoryDisplayName = product.getCategoryDisplayName();
        this.keywords.addAll(keywords.stream().map(Keyword::getName).toList());
        this.status = product.getStatus();
    }
}