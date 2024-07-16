package clov3r.oneit_server.repository;

import clov3r.oneit_server.config.MapSerializer;
import clov3r.oneit_server.domain.Keyword;
import clov3r.oneit_server.domain.collectioin.KeyValue;
import clov3r.oneit_server.domain.data.Gender;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.spi.LocaleNameProvider;

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

