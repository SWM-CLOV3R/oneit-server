package clov3r.oneit_server.domain.entity;

import clov3r.oneit_server.domain.data.Gender;
import clov3r.oneit_server.domain.data.status.ProductStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Table(name = "product")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "name")
    private String name;

    @Column(name = "original_price")
    private int originalPrice;

    @Column(name = "current_price")
    private int currentPrice;

    @Column(name = "discount_rate")
    private int discountRate;

    @Column(name = "mall_name")
    private String mallName;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "brand_description")
    private String brandDescription;

    @Column(name = "product_url")
    private String productUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    private Gender gender;  // FEMALE, MALE, UNISEX

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_idx")
    private Category category;

    @Column(name = "category_display_name")
    private String categoryDisplayName;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Column(updatable = false, insertable = false)
    private List<ProductKeyword> productKeywords = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Override
    public String toString() {
        return "Product{" +
                "idx=" + idx +
                ", name='" + name + '\'' +
                ", originalPrice=" + originalPrice +
                ", currentPrice=" + currentPrice +
                ", discountRate=" + discountRate +
                ", mallName='" + mallName + '\'' +
                ", brandName='" + brandName + '\'' +
                ", brandDescription='" + brandDescription + '\'' +
                ", productUrl='" + productUrl + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", gender=" + gender +
                ", category=" + category +
                ", categoryDisplayName='" + categoryDisplayName + '\'' +
                ", productKeywords=" + productKeywords +
                '}';
    }
}
