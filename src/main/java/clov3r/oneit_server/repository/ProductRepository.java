package clov3r.oneit_server.repository;

import clov3r.oneit_server.domain.Product;
import clov3r.oneit_server.domain.data.Gender;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final EntityManager em;
    private final KeywordRepository keywordRepository;

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
        return query.getResultList();
    }

    private List<Long> getKeywordIdxList(List<String> keywords) {
         return keywordRepository.findIdsByKeywords(keywords);
    }

}

