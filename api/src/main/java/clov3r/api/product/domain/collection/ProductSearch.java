package clov3r.api.product.domain.collection;

import clov3r.domain.domains.type.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearch {

    @Enumerated(EnumType.STRING)
    private Gender gender;
    private int age;
    private int minPrice;
    private int maxPrice;
    private HashMap<Integer, String> keywords = new HashMap<>();
//    private List<QuestionKeyword> keywordsList = new ArrayList<>();
}

