package clov3r.api.product.domain.data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class QuestionKeyword {
    private int questionIdx;
    private String keyword;
}
