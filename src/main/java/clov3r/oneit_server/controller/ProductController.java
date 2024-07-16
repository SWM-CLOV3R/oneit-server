package clov3r.oneit_server.controller;

import clov3r.oneit_server.domain.Keyword;
import clov3r.oneit_server.domain.Product;
import clov3r.oneit_server.domain.collectioin.KeyValue;
import clov3r.oneit_server.domain.collectioin.MatchedProduct;
import clov3r.oneit_server.domain.data.Gender;
import clov3r.oneit_server.repository.ProductRepository;
import clov3r.oneit_server.repository.ProductSearch;
import clov3r.oneit_server.response.BaseResponse;
import clov3r.oneit_server.response.BaseResponseStatus;
import clov3r.oneit_server.service.KeywordService;
import clov3r.oneit_server.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static clov3r.oneit_server.response.BaseResponseStatus.*;
import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final KeywordService keywordService;
    private final ProductRepository productRepository;

    @Tag(name = "선물 추천 API", description = "선물 추천 API 목록")
    @Operation(
            summary = "성별, 가격, 키워드 필터링 API",
            description = "성별, 가격, 키워드에 해당하는 상품 리스트를 추출합니다. " +
                    "키워드 일치 개수가 많은 순서대로 출력되며 키워드 매칭 개수가 0인 경우 제외됩니다. " +
                    "개수가 5개 미만인 경우 그대로 출력됩니다. ")


    @PostMapping("/api/v1/product/result")
    public BaseResponse<List<ProductDTO>> extractProducts(@ModelAttribute("ProductSearch") ProductSearch productSearch) {

        // check gender
//        if (!Gender.isValid(productSearch.getGender())) {
//            return new BaseResponse<>(REQUEST_GENDER_ERROR);
//        }
        // check price
        if (productSearch.getMinPrice() < 0 || productSearch.getMaxPrice() < 0 || productSearch.getMinPrice() > productSearch.getMaxPrice()) {
            return new BaseResponse<>(REQUEST_PRICE_ERROR);
        }
        // check keywords
        if (productSearch.getKeywords() == null) {
            return new BaseResponse<>(REQUEST_ERROR);
        }

        // extract keyword string list from keywords object of productSearch
        List<String> keywordList = productSearch.getKeywords().values().stream().toList();
        if (!keywordService.existsByKeyword(keywordList)) {
            return new BaseResponse<>(DATABASE_ERROR_NOT_FOUND);
        }

        List<Product> products = productService.filterProducts(productSearch);
        // max 5 products
        if (products.size() > 5) {
            products = products.subList(0, 5);
        }
        List<ProductDTO> productDTOs = products.stream()
                .map(product -> new ProductDTO(product, keywordService.getKeywordsByIdx(product.getIdx())))
                .toList();

        return new BaseResponse<>(productDTOs);
    }

    @PostMapping("/api/v1/product/result/category")
    public BaseResponse<List<ProductDTO>> extractProductsWithCategory(@RequestBody ProductSearch productSearch) {

        // check gender
//        if (!Gender.isValid(productSearch.getGender())) {
//            return new BaseResponse<>(REQUEST_GENDER_ERROR);
//        }
        // check price
        if (productSearch.getMinPrice() <0 || productSearch.getMaxPrice() < 0 || productSearch.getMinPrice() > productSearch.getMaxPrice()) {
            return new BaseResponse<>(REQUEST_PRICE_ERROR);
        }
        // check keywords
        if (productSearch.getKeywords() == null) {
            return new BaseResponse<>(REQUEST_ERROR);
        }
        if (!keywordService.existsByKeyword(productSearch.getKeywords().values().stream().toList())) {
            return new BaseResponse<>(DATABASE_ERROR_NOT_FOUND);
        }

        List<Product> products = productService.filterProductsWithCategory(productSearch);
        // max 5 products
        if (products.size() > 5) {
            products = products.subList(0, 5);
        }
        List<ProductDTO> productDTOs = products.stream()
                .map(product -> new ProductDTO(product, keywordService.getKeywordsByIdx(product.getIdx())))
                .toList();

        return new BaseResponse<>(productDTOs);
    }

    @Tag(name = "선물 추천 API", description = "선물 추천 API 목록")
    @Operation(summary = "가격 필터링", description = "가격대로 상품을 필터링하고 랜덤으로 5개의 상품을 반환합니다. 결과 제품개수가 5개 미만일 경우 결과 개수 그대로 반환합니다. (키워드 선정하기 전 프론트 연동 테스트용 API 입니다. 성별, 나이는 디폴값으로 요청해도 동작합니다. 가격은 0 <= min < max 이어야 하고, keywords 의 key값은 질문의 번호, 즉 integer 값이어야 합니다.)")
    @PostMapping("/api/v1/product/result/price")
    public BaseResponse<List<ProductDTO>> extractProductByPrice(@RequestBody ProductSearch productSearch) {

        // 1. check price
        if (productSearch.getMinPrice() <0 || productSearch.getMaxPrice() < 0 || productSearch.getMinPrice() > productSearch.getMaxPrice()) {
            return new BaseResponse<>(REQUEST_PRICE_ERROR);
        }
        // filter products by price
        List<Product> products = productRepository.filterProductsByPrice(productSearch);

        // return five random products with no redundancy
        // if there are less than five products, return all products
        List<Product> randomProducts = new ArrayList<>();
        int maxSize = Math.min(products.size(), 5);
        for (int i = 0; i < maxSize; i++) {
            int randomIndex = (int) (Math.random() * products.size());
            randomProducts.add(products.get(randomIndex));
        }
        List<ProductDTO> productDTOs = randomProducts.stream()
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
        private Gender gender;

        public ProductDTO(Product product, List<Keyword> keywords) {
            this.productIdx = product.getIdx();
            this.name = product.getName();
            this.originalPrice = product.getOriginalPrice();
            this.shoppingmall = product.getMallName();
            this.productUrl = product.getProductUrl();
            this.thumbnailUrl = product.getThumbnailUrl();
            this.keywords.addAll(keywords);
            this.gender = product.getGender();
        }
    }

    @Data
    static class KeywordDTO {
        private Long idx;
        private String name;
        private String description;

        public KeywordDTO(Keyword keyword) {
            this.idx = keyword.getIdx();
            this.name = keyword.getName();
            this.description = keyword.getDescription();
        }
    }

}
