package clov3r.oneit_server.domain;

import jakarta.persistence.*;

@Entity
public class ProductKeyword {

    @Id
    @GeneratedValue
    @Column(name = "product_keyword_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_idx")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "keyword_idx")
    private Keyword keyword;
    private int intensity;

}
