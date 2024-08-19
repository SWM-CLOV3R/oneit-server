package clov3r.oneit_server.domain.collection;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class QuestionCategory {

    private int questionIdx;
    private List<Long> categoryIdxList = new ArrayList<>();

    public QuestionCategory(int questionIdx) {
        this.questionIdx = questionIdx;
    }

}
