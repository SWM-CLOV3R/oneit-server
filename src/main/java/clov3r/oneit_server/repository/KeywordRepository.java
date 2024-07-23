package clov3r.oneit_server.repository;

import clov3r.oneit_server.domain.entity.Keyword;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class KeywordRepository {

    private final EntityManager em;


    public List<Long> findIdsByKeywords(List<String> keywords) {
        return em.createQuery("select k.idx from Keyword k where k.name in :keywords", Long.class)
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

    /**
     * 키워드가 존재하는지 확인
     * @param keyword
     * @return
     */
    public Boolean existsByKeyword(String keyword) {
        return em.createQuery("select count(k) > 0 from Keyword k where k.name = :keyword", Boolean.class)
                .setParameter("keyword", keyword)
                .getSingleResult();
    }


    /**
     * 키워드 String 리스트를 받아서 해당 키워드들의 idx 리스트를 반환
     * @param keywordList
     * @return List<Long>
     */
    List<Long> getKeywordIdxList(List<String> keywordList) {
        // only using keyword string from hash value to extract keyword idx list
        return em.createQuery("select k.idx from Keyword k where k.name in :keywordList", Long.class)
                .setParameter("keywordList", keywordList)
                .getResultList();}

    public List<Long> getProductKeywordIdxList(Long productIdx) {
        return em.createQuery("select pk.keyword.idx from ProductKeyword pk where pk.product.idx = :productIdx", Long.class)
                .setParameter("productIdx", productIdx)
                .getResultList();
    }

    public Long findKeywordIdx(String keyword) {
        return em.createQuery("select k.idx from Keyword k where k.name = :keyword", Long.class)
                .setParameter("keyword", keyword)
                .getSingleResult();
    }

}
