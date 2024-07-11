package clov3r.oneit_server.controller;

import clov3r.oneit_server.domain.Keyword;
import clov3r.oneit_server.domain.Product;
import clov3r.oneit_server.domain.data.Gender;
import clov3r.oneit_server.repository.ProductRepository;
import clov3r.oneit_server.repository.ProductSearch;
import clov3r.oneit_server.response.BaseResponse;
import clov3r.oneit_server.response.BaseResponseStatus;
import clov3r.oneit_server.service.KeywordService;
import clov3r.oneit_server.service.ProductService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static clov3r.oneit_server.response.BaseResponseStatus.REQUEST_ERROR;
import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final KeywordService keywordService;
    private final ProductRepository productRepository;

    @PostMapping("/api/v1/product/result")
    public BaseResponse<List<ProductDTO>> extractProduct(@RequestBody ProductSearch productSearch) {

        // check gender
        if (!Gender.isValid(productSearch.getGender())) {
            return new BaseResponse<>(REQUEST_ERROR);
        }
        // check age
        // check price
        if (productSearch.getMinPrice() <0 || productSearch.getMaxPrice() < 0 || productSearch.getMinPrice() > productSearch.getMaxPrice()) {
            return new BaseResponse<>(REQUEST_ERROR);
        }
        // check keywords

        List<Product> products = productRepository.filterProducts(productSearch);
        List<ProductDTO> productDTOs = products.stream()
                .map(product -> new ProductDTO(product, keywordService.getKeywordsByIdx(product.getIdx())))
                .toList();

        return new BaseResponse<>(productDTOs);
    }

    @Data
    static class ProductDTO {
        private Long productIdx;
        private String name;
        private int originalPrice;
        private String shoppingmall;
        private String productUrl;
        private String thumbnailUrl;
        private Long categoryIdx;
        private List<Keyword> keywords  = new ArrayList<>();
        private String gender;

        public ProductDTO(Product product, List<Keyword> keywords) {
            this.productIdx = product.getIdx();
            this.name = product.getName();
            this.originalPrice = product.getOriginalPrice();
            this.shoppingmall = product.getShoppingmall();
            this.productUrl = product.getProductUrl();
            this.thumbnailUrl = product.getThumbnailUrl();
            this.categoryIdx = product.getCategoryIdx();
            this.keywords.addAll(keywords);
            this.gender = product.getGender();
        }
    }

    @Data
    static class KeywordDTO {
        private Long keywordIdx;
        private String keyword;
        private String description;

        public KeywordDTO(Keyword keyword) {
            this.keywordIdx = keyword.getIdx();
            this.keyword = keyword.getKeyword();
            this.description = keyword.getDescription();
        }
    }

}
