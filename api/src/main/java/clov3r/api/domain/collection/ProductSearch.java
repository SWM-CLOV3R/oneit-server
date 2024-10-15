package clov3r.api.domain.collection;

import static jakarta.persistence.EnumType.STRING;

import clov3r.api.domain.data.Gender;
import jakarta.persistence.Enumerated;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearch {

    @Enumerated(value = STRING)
    private Gender gender;
    private int age;
    private int minPrice;
    private int maxPrice;
    private HashMap<Integer, String> keywords = new HashMap<>();
//    private List<QuestionKeyword> keywordsList = new ArrayList<>();
}

