package clov3r.api.product.domain.collection;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class QuestionCategory {

    private int questionIdx;
    private List<Long> categoryIdxList = new ArrayList<>();

    public QuestionCategory(int questionIdx) {
        this.questionIdx = questionIdx;
    }

}