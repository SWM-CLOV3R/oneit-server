package clov3r.oneit_server.domain.entity;

import clov3r.oneit_server.domain.collection.ProductKeywordId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Setter
@Entity
@Table(name = "product_keyword")
public class ProductKeyword {

    @EmbeddedId
    private ProductKeywordId id;

    @ManyToOne(fetch = LAZY)
    @MapsId("productIdx") // Maps productIdx in ProductKeywordId
    @JoinColumn(name = "product_idx", referencedColumnName = "idx")
    private Product product;

    @ManyToOne(fetch = LAZY)
    @MapsId("keywordIdx") // Maps keywordIdx in ProductKeywordId
    @JoinColumn(name = "keyword_idx", referencedColumnName = "idx")
    private Keyword keyword;

    private Integer intensity;

}
