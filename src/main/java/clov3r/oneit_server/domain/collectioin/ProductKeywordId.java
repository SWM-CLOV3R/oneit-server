package clov3r.oneit_server.domain.collectioin;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProductKeywordId implements Serializable {

    @Column(name = "product_idx")
    private Long productIdx;

    @Column(name = "keyword_idx")
    private Long keywordIdx;

    public ProductKeywordId() {}

    public ProductKeywordId(Long productIdx, Long keywordIdx) {
        this.productIdx = productIdx;
        this.keywordIdx = keywordIdx;
    }

    // Getters and setters

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductKeywordId)) return false;
        ProductKeywordId that = (ProductKeywordId) o;
        return Objects.equals(productIdx, that.productIdx) &&
                Objects.equals(keywordIdx, that.keywordIdx);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productIdx, keywordIdx);
    }

}
