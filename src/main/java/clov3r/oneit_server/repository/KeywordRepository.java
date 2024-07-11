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

    public List<Keyword> findKeywordByIdx(List<Long> keywordIdxs) {
        return em.createQuery("select k from Keyword k where k.idx in :keywordIdxs", Keyword.class)
                .setParameter("keywordIdxs", keywordIdxs)
                .getResultList();

    }
}
