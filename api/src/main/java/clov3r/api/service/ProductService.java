package clov3r.api.service;

import clov3r.api.domain.DTO.ProductSummaryDTO;
import clov3r.api.domain.collection.ProductSearch;
import clov3r.api.domain.entity.Product;
import clov3r.api.repository.KeywordRepository;
import clov3r.api.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Product getProductByIdx(Long productIdx) {
        Product product = productRepository.findById(productIdx);
//        System.out.println("product.toString() = " + product.toString());
        return product;
    }

    public List<Product> filterProductsWithCategory(ProductSearch productSearch) {


        List<Product> initialFilteredProducts = productRepository.filterProductsByPriceAndGender(productSearch);

        // filter by category and answer keywords
        List<Product> filterProductsByCategoryKeywords = productRepository.filterProductsByCategoryAndKeywords(initialFilteredProducts, productSearch);

        if (!productSearch.getKeywords().isEmpty()) {
            return productRepository.filterProductsByKeywords(filterProductsByCategoryKeywords, productSearch.getKeywords());
        }

        return initialFilteredProducts;
    }

    public List<ProductSummaryDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductSummaryDTO::new).toList();
    }

    public List<ProductSummaryDTO> getProductListPagination(Long lastProductIdx, int pageSize) {
        return productRepository.findProductListPagination(lastProductIdx, pageSize);
    }

  public List<ProductSummaryDTO> searchProduct(String searchKeyword) {
    List<Product> products = productRepository.searchProduct(searchKeyword);
    return products.stream()
        .map(ProductSummaryDTO::new).toList();
  }
}
