package clov3r.api.common.repository;

import clov3r.api.common.domain.entity.Category;
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