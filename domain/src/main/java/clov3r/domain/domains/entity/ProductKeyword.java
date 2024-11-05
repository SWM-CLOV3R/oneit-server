package clov3r.domain.domains.entity;

import clov3r.domain.domains.entity.id.ProductKeywordId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_keyword")
public class ProductKeyword {

    @EmbeddedId
    private ProductKeywordId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productIdx") // Maps productIdx in ProductKeywordId
    @JoinColumn(name = "product_idx", referencedColumnName = "idx")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("keywordIdx") // Maps keywordIdx in ProductKeywordId
    @JoinColumn(name = "keyword_idx", referencedColumnName = "idx")
    private Keyword keyword;

    private Integer intensity;

}
