package clov3r.oneit_server.domain;

import clov3r.oneit_server.domain.data.Gender;
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
public class Product {

    @Id
    @GeneratedValue
    @Column(name = "idx")
    private Long idx;

    @Column(name = "name")
    private String name;

    @Column(name = "original_price")
    private int originalPrice;

    @Column(name = "mall_name")
    private String mallName;

    @Column(name = "product_url")
    private String productUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    private Gender gender;  // FEMALE, MALE, UNISEX

    @ManyToOne
    @JoinColumn(name = "category_idx")
    private Category category;
    private String large;
    private String middle;
    private String small;

    @Column(name = "category_display_name")
    private String categoryDisplayName;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @Column(updatable = false, insertable = false)
    private List<ProductKeyword> productKeywords = new ArrayList<>();



}
