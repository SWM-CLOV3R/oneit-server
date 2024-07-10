package clov3r.oneit_server.repository;

import clov3r.oneit_server.domain.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final EntityManager em;

    public List<Product> filterProducts(ProductSearch productSearch) {
        int minPrice = productSearch.getMinPrice();
        int maxPrice = productSearch.getMaxPrice();
        String jpql = "select p from Product p where p.originalPrice > :minPrice and p.originalPrice < :maxPrice";
        TypedQuery<Product> query = em.createQuery(jpql, Product.class);
        query.setParameter("minPrice", minPrice);
        query.setParameter("maxPrice", maxPrice);
        return query.getResultList();
    }

}

