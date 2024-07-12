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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
    @Operation(summary = "성별, 가격, 키워드 필터링 API", description = "성별, 가격, 키워드에 해당하는 상품 리스트를 추출합니다. (현재 서버 DB에 키워드 데이터가 아직 없어 4000번대 에러가 나옴)")
    @PostMapping("/api/v1/product/result")
    public BaseResponse<List<ProductDTO>> extractProduct(@RequestBody ProductSearch productSearch) {

        // check gender
        if (!Gender.isValid(productSearch.getGender())) {
            return new BaseResponse<>(REQUEST_GENDER_ERROR);
        }
        // check age
        // check price
        if (productSearch.getMinPrice() <0 || productSearch.getMaxPrice() < 0 || productSearch.getMinPrice() > productSearch.getMaxPrice()) {
            return new BaseResponse<>(REQUEST_PRICE_ERROR);
        }
        // check keywords
        if (productSearch.getKeywords() == null) {
            productSearch.setKeywords(new ArrayList<>());
        }
        if (!keywordService.existsByKeyword(productSearch.getKeywords())) {
            return new BaseResponse<>(DATABASE_ERROR_NOT_FOUND);
        }
        // validate redundant keywords
        List<String> keywords = productSearch.getKeywords().stream().distinct().toList();
        productSearch.setKeywords(keywords);

        List<Product> products = productRepository.filterProducts(productSearch);
        List<ProductDTO> productDTOs = products.stream()
                .map(product -> new ProductDTO(product, keywordService.getKeywordsByIdx(product.getIdx())))
                .toList();

        return new BaseResponse<>(productDTOs);
    }

    @Tag(name = "선물 추천 API", description = "선물 추천 API 목록")
    @Operation(summary = "가격 필터링", description = "가격대로 상품을 필터링하고 랜덤으로 5개의 상품을 반환합니다. 결과 제품개수가 5개 미만일 경우 결과 개수 그대로 반환합니다. (키워드 선정하기 전 프론트 연동 테스트용 API 입니다. 가격 정보 이외에는 디폴값으로 요청해도 동작합니다.)")
    @PostMapping("/api/v1/product/result/price")
    public BaseResponse<List<ProductDTO>> extractProductByPrice(@RequestBody ProductSearch productSearch) {
        // check price
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
