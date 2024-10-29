package clov3r.api.common.service;

import clov3r.api.common.domain.entity.Category;
import clov3r.api.common.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category getCategoryByIdx(Long idx) {
        return categoryRepository.findCategoryByIdx(idx);
    }
}
