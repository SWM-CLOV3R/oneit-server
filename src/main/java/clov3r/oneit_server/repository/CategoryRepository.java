package clov3r.oneit_server.repository;

import clov3r.oneit_server.domain.entity.Category;
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
