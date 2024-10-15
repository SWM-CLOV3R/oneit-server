package clov3r.api.repository;

import clov3r.api.domain.entity.Category;
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
