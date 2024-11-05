package clov3r.domain.domains.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * 복합키 product_idx, keyword_idx를 가지는 ProductKeywordId 클래스
 */
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
