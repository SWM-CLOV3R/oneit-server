package clov3r.oneit_server.service;

import clov3r.oneit_server.domain.Keyword;
import clov3r.oneit_server.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
