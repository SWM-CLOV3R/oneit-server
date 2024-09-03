package clov3r.oneit_server.domain.collection;

import static jakarta.persistence.EnumType.STRING;

import clov3r.oneit_server.domain.data.Gender;
import clov3r.oneit_server.domain.data.QuestionKeyword;
import jakarta.persistence.Enumerated;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

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

