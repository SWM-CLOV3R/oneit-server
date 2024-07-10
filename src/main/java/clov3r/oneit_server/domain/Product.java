package clov3r.oneit_server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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

    private String gender;  // FEMALE, MALE, UNISEX

}
