package clov3r.oneit_server.domain.collectioin;

import clov3r.oneit_server.domain.data.Gender;
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
    // List of key-value data
//    @JsonSerialize(using = MapSerializer.class)
//    private List<KeyValue<Integer, String>> keywords = new ArrayList<>();
    private HashMap<Integer, String> keywords = new HashMap<>();

}

