package clov3r.oneit_server.repository;

import clov3r.oneit_server.domain.Product;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final EntityManager em;

    public void save(Product product) {
        em.persist(product);
    }
}

