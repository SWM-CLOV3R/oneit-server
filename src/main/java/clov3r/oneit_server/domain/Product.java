package clov3r.oneit_server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue
    @Column(name = "product_idx")
    private Long idx;

    @Column(name = "product_name")
    private String name;

    @Column(name = "original_price")
    private int originalPrice;

    private String shoppingmall;

    @Column(name = "product_url")
    private String productUrl;

    @Column(name = "thumbnail")
    private String thumbnailUrl;

    @Column(name = "category_idx")
    private Long categoryIdx;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductKeyword> productKeywords = new ArrayList<>();

    private String gender;  // FEMALE, MALE, UNISEX

}
