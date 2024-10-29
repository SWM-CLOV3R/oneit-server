package clov3r.api.product.domain.collection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchedProduct {

    private Long idx;
    private int matchedKeywordCount;
    private int matchedScore;

    public MatchedProduct(Long idx) {
        this.idx = idx;
        this.matchedKeywordCount = 0;
        this.matchedScore = 0;
    }

    public MatchedProduct(Long idx, int matchedKeywordCount) {
        this.idx = idx;
        this.matchedKeywordCount = matchedKeywordCount;
    }

    public void addMatchedKeywordCount() {
        this.matchedKeywordCount++;
    }


    public void addMatchedScore(int i) {
        this.matchedScore += i;
    }
}
