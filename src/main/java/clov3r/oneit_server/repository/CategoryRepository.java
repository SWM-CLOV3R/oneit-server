package clov3r.oneit_server.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final EntityManager em;

    public Long findParentCategoryIdx(Long childCategoryIdx) {
        return em.createQuery("select c.parentCategory.idx from Category c where c.idx = :childCategoryIdx", Long.class)
                .setParameter("childCategoryIdx", childCategoryIdx)
                .getSingleResult();
    }

}
