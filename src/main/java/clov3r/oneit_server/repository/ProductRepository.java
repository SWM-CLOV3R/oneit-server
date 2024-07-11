package clov3r.oneit_server.repository;

import clov3r.oneit_server.controller.ProductController;
import clov3r.oneit_server.domain.Keyword;
import clov3r.oneit_server.domain.Product;
import clov3r.oneit_server.domain.data.Gender;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.valves.HealthCheckValve;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final EntityManager em;
    private final KeywordRepository keywordRepository;
    private final PropertyResolverUtils propertyResolverUtils;

    public List<Product> filterProducts(ProductSearch productSearch) {
        // First Query: Filter by price and gender
        String jpql = buildInitialQuery(productSearch);
        TypedQuery<Product> query = em.createQuery(jpql, Product.class);
        setInitialQueryParameters(query, productSearch);
        List<Product> initialFilteredProducts = query.getResultList();

        // Second Query: Further filter by keywords if applicable
        if (!productSearch.getKeywords().isEmpty()) {
            return filterByKeywords(initialFilteredProducts, productSearch.getKeywords());
        }

        return initialFilteredProducts;
    }


    private String buildInitialQuery(ProductSearch productSearch) {
        String jpql = "select p from Product p where p.originalPrice > :minPrice and p.originalPrice < :maxPrice";
        if (!productSearch.getGender().equals(Gender.UNISEX.toString())) {
            jpql += " and p.gender <> :excludedGender";
        }
        return jpql;
    }

    private void setInitialQueryParameters(TypedQuery<Product> query, ProductSearch productSearch) {
        query.setParameter("minPrice", productSearch.getMinPrice());
        query.setParameter("maxPrice", productSearch.getMaxPrice());
        if (!productSearch.getGender().equals(Gender.UNISEX.toString())) {
            String excludedGender = productSearch.getGender().equals(Gender.MALE.toString()) ? Gender.FEMALE.toString() : Gender.MALE.toString();
            query.setParameter("excludedGender", excludedGender);
        }
    }

    private List<Product> filterByKeywords(List<Product> products, List<String> keywords) {
        if (products.isEmpty() || keywords.isEmpty()) {
            return products;
        }

        List<Long> productIds = products.stream().map(Product::getIdx).collect(Collectors.toList());
        List<Long> keywordIdxList = getKeywordIdxList(keywords);
        for (Product product : products) {
            System.out.println("Product ID: " + product.getIdx());
        }
        for (Long keywordIdx : keywordIdxList) {
            System.out.println("Keyword ID: " + keywordIdx);
        }

        if (keywordIdxList.isEmpty()) {
            return products; // No matching keywords found
        }

        // product list that match all keywords in initialFilteredProducts
        String jpql = "select p from Product p " +
                "join p.productKeywords pk " +
                "where p.idx in :productIds " +
                "and pk.keyword.idx in :keywordIdxList " +
                "group by p.idx having count(pk) = :keywordCount";
        TypedQuery<Product> query = em.createQuery(jpql, Product.class);
        query.setParameter("productIds", productIds);
        query.setParameter("keywordIdxList", keywordIdxList);
        query.setParameter("keywordCount", (long) keywords.size());
        List<Product> resultProducts = query.getResultList();
        for (Product product : resultProducts) {
            System.out.println("Product ID: " + product.getIdx());
        }
        return resultProducts;
    }

    // Method to get keyword IDs from keyword strings
    private List<Long> getKeywordIdxList(List<String> keywords) {
        // Assuming there's a method in KeywordRepository or similar to fetch IDs by keyword strings
        // KeywordRepository keywordRepository = ...;
         return keywordRepository.findIdsByKeywords(keywords);
//        return keywords.stream().map(this::findKeywordIdByString).collect(Collectors.toList());
    }

}

