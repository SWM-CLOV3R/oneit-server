package clov3r.oneit_server.repository;

import clov3r.oneit_server.controller.ProductController;
import clov3r.oneit_server.domain.Product;
import clov3r.oneit_server.domain.data.Gender;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final EntityManager em;

    public List<Product> filterProducts(ProductSearch productSearch) {


        int minPrice = productSearch.getMinPrice();
        int maxPrice = productSearch.getMaxPrice();
        String jpql = "select p from Product p where p.originalPrice > :minPrice and p.originalPrice < :maxPrice";

        String gender = productSearch.getGender();
        System.out.println("gender = " + gender);
        String excludedGender = "";

        if (!gender.equals(Gender.UNISEX.toString())) {
            if (gender.equals(Gender.MALE.toString())) {
                excludedGender = Gender.FEMALE.toString();
            }
            if (gender.equals(Gender.FEMALE.toString())) {
                excludedGender = Gender.MALE.toString();
            }

            jpql += " and p.gender <> :excludedGender";
        }

        TypedQuery<Product> query = em.createQuery(jpql, Product.class);
        query.setParameter("minPrice", minPrice);
        query.setParameter("maxPrice", maxPrice);

        if (!gender.equals(Gender.UNISEX.toString())) {
            query.setParameter("excludedGender", excludedGender);
        }

        return query.getResultList();
    }

}

