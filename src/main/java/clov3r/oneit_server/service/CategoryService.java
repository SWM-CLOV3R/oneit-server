package clov3r.oneit_server.service;

import clov3r.oneit_server.domain.entity.Category;
import clov3r.oneit_server.repository.CategoryRepository;
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
