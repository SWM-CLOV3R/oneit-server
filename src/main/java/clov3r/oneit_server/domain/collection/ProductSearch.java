package clov3r.oneit_server.domain.collection;

import clov3r.oneit_server.domain.data.Gender;
import clov3r.oneit_server.domain.data.QuestionKeyword;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class ProductSearch {
    private Gender gender;
    private int age;
    private int minPrice;
    private int maxPrice;
    private HashMap<Integer, String> keywords = new HashMap<>();
//    private List<QuestionKeyword> keywordsList = new ArrayList<>();
}

