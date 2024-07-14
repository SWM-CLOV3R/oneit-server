package clov3r.oneit_server.repository;

import clov3r.oneit_server.domain.Keyword;
import clov3r.oneit_server.domain.data.Gender;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.spi.LocaleNameProvider;

@Getter
@Setter
public class ProductSearch {
    private String gender;
    private int age;
    private int minPrice;
    private int maxPrice;
    private HashMap<Integer, String> keywords = new HashMap<>();

    // add keywords
    public void addKeywords(HashMap<Integer, String> keywords) {
        this.keywords.putAll(keywords);
    }

}

