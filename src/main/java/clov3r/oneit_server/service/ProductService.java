package clov3r.oneit_server.service;

import clov3r.oneit_server.domain.Product;
import clov3r.oneit_server.repository.KeywordRepository;
import clov3r.oneit_server.repository.ProductRepository;
import clov3r.oneit_server.repository.ProductSearch;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final KeywordRepository keywordRepository;


    public List<Product> filterProducts(ProductSearch productSearch) {
        // First Query: Filter by price and gender
        List<Product> initialFilteredProducts = productRepository.filterProductsByPriceAndGender(productSearch);

        // Second Query: Further filter by keywords if applicable
        if (!productSearch.getKeywords().isEmpty()) {
            return productRepository.filterProductsByKeywords(initialFilteredProducts, productSearch.getKeywords());
        }

        return initialFilteredProducts;
    }
}
