package clov3r.api.product.service;

import clov3r.api.product.domain.entity.Keyword;
import clov3r.api.product.repository.KeywordRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    public List<Keyword> getKeywordsByIdx(Long productIdx) {
        List<Keyword> keywordByProductIdx = keywordRepository.findKeywordByProductIdx(productIdx);
        return keywordByProductIdx;
    }

    public Boolean existsByKeyword(List<String> keywords) {
        // Check if any of the keywords exist in the database
        for (String keyword : keywords) {
            if (!keywordRepository.existsByKeyword(keyword)) {
                return false;
            }
        }
        return true;
    }
}
