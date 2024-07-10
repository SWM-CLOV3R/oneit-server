package clov3r.oneit_server.repository;

import clov3r.oneit_server.domain.Keyword;
import clov3r.oneit_server.domain.data.Gender;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductSearch {
    private String gender;
    private int age;
    private int minPrice;
    private int maxPrice;
    private List<Keyword> keywords = new ArrayList<>();
}

