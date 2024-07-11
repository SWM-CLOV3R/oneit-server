package clov3r.oneit_server.repository;

import clov3r.oneit_server.domain.Keyword;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class KeywordRepository {

    private final EntityManager em;


    public List<Long> findIdsByKeywords(List<String> keywords) {
        return em.createQuery("select k.idx from Keyword k where k.keyword in :keywords", Long.class)
                .setParameter("keywords", keywords)
                .getResultList();
    }

    public List<Keyword> findKeywordByProductIdx(Long productIdx) {
        return em.createQuery("select k from Keyword k " +
                        "join ProductKeyword pk on k.idx = pk.keyword.idx " +
                        "where pk.product.idx = :productIdx", Keyword.class)
                .setParameter("productIdx", productIdx)
                .getResultList();
    }

    public Boolean existsByKeyword(String keyword) {
        return em.createQuery("select count(k) > 0 from Keyword k where k.keyword = :keyword", Boolean.class)
                .setParameter("keyword", keyword)
                .getSingleResult();
    }
}
