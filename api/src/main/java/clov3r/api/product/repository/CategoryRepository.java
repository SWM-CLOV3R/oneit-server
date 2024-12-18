package clov3r.api.product.repository;

import clov3r.domain.domains.entity.Category;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {
    private final EntityManager em;
    public Category findCategoryByIdx(Long idx) {
        return em.find(Category.class, idx);
    }
}
