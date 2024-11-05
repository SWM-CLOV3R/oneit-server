package clov3r.api.product.service;

import clov3r.api.product.repository.CategoryRepository;
import clov3r.domain.domains.entity.Category;
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
