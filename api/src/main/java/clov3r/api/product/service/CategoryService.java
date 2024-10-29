package clov3r.api.product.service;

import clov3r.api.product.domain.entity.Category;
import clov3r.api.product.repository.CategoryRepository;
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
