package clov3r.api.controller;

import static clov3r.api.error.errorcode.CommonErrorCode.DATABASE_ERROR_NOT_FOUND;
import static clov3r.api.error.errorcode.CustomErrorCode.*;
import static clov3r.api.error.errorcode.CommonErrorCode.*;

import clov3r.api.domain.DTO.ProductDTO;
import clov3r.api.domain.DTO.ProductDetailDTO;
import clov3r.api.domain.DTO.ProductSummaryDTO;
import clov3r.api.domain.collection.ProductFilter;
import clov3r.api.domain.collection.ProductSearch;
import clov3r.api.domain.data.Gender;
import clov3r.api.domain.entity.Category;
import clov3r.api.domain.entity.Keyword;
import clov3r.api.domain.entity.Product;
import clov3r.api.error.errorcode.CommonErrorCode;
import clov3r.api.error.exception.BaseExceptionV2;
import clov3r.api.repository.ProductRepository;
import clov3r.api.service.CategoryService;
import clov3r.api.service.KeywordService;
import clov3r.api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductControllerV2 {

    private final ProductService productService;
    private final KeywordService keywordService;
    private final CategoryService categoryService;
    private final ProductRepository productRepository;


    @Tag(name = "선물 추천 API", description = "선물 추천 API 목록")
    @Operation(
            summary = "성별, 가격, 키워드, 카테고리 반영한 필터링 API",
            description = "성별, 가격에 해당하는 상품 리스트 중에서 " +
                    "각 질문에 해당하는 카테고리의 제품 중 키워드를 만족시키는 상품들을 모두 가져온 뒤, " +
                    "그 중 키워드 일치 개수가 많은 순서대로 출력되며 키워드 매칭 개수가 0인 경우 제외됩니다. " +
                    "개수가 5개 미만인 경우 그대로 출력됩니다. ")
    @PostMapping("/api/v2/product/result/category")
    public ResponseEntity<List<ProductDTO>> extractProductsWithCategory(
        @RequestBody ProductSearch productSearch
    ) {

        // check gender
        if (!Gender.isValid(productSearch.getGender())) {
            log.info(REQUEST_GENDER_ERROR.getMessage());
            throw new BaseExceptionV2(REQUEST_GENDER_ERROR);
        }
        // check price
        if (productSearch.getMinPrice() <0 || productSearch.getMaxPrice() < 0 || productSearch.getMinPrice() > productSearch.getMaxPrice()) {
            throw new BaseExceptionV2(REQUEST_PRICE_ERROR);
        }
        // check keywords
        if (productSearch.getKeywords() == null) {
            throw new BaseExceptionV2(REQUEST_ERROR);
        }
        if (!keywordService.existsByKeyword(productSearch.getKeywords().values().stream().toList())) {
            throw new BaseExceptionV2(DATABASE_ERROR_NOT_FOUND);
        }

        List<Product> products = productService.filterProductsWithCategory(productSearch);
        // max 5 products
        if (products.size() > 5) {
            products = products.subList(0, 5);
        }
        List<ProductDTO> productDTOs = products.stream()
                .map(product -> new ProductDTO(product, keywordService.getKeywordsByIdx(product.getIdx())))
                .toList();

        if (productDTOs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(productDTOs);
    }


    @Tag(name = "상품 API", description = "상품 관련 API 목록")
    @Operation(summary = "상품 상세 정보 조회")
    @GetMapping("/api/v2/products/{productIdx}")
    public ResponseEntity<ProductDetailDTO> getProductDetail(@PathVariable Long productIdx) {
        if (!productRepository.existsProduct(productIdx)) {
            throw new BaseExceptionV2(PRODUCT_NOT_FOUND);
        }
        // get Category
        Product product = productService.getProductByIdx(productIdx);
        Category category = categoryService.getCategoryByIdx(product.getCategory().getIdx());

        // get keywords
        List<Keyword> keywords = keywordService.getKeywordsByIdx(productIdx);
        ProductDetailDTO productDetailDTO;
        try {
            productDetailDTO = new ProductDetailDTO(product, keywords, category);
        } catch (Exception e) {
            throw new BaseExceptionV2(PRODUCT_DTO_ERROR);
        }

        return ResponseEntity.ok(productDetailDTO);
    }

    @Tag(name = "상품 API", description = "상품 관련 API 목록")
    @Operation(summary = "상품 리스트 조회 - 페이지네이션", description = "마지막 상품 인덱스와 페이지 사이즈를 입력받아 페이지네이션된 상품 리스트를 반환합니다." +
            "LastproductIdx가 null 일 경우 처음부터 페이지네이션됩니다. " +
            "즉, 첫 페이지에서는 LastProductIdx를 입력하지 않아야 합니다.")
    @GetMapping("/api/v2/products")
    public ResponseEntity<List<ProductSummaryDTO>> getProductListPagination(
        @RequestParam(required = false) Long LastProductIdx,
        @RequestParam(required = false) Integer pageSize)
    {

        if (LastProductIdx == null && pageSize == null) {
            return ResponseEntity.ok(productService.getAllProducts());
        }
        List<ProductSummaryDTO> products = productService.getProductListPagination(LastProductIdx, pageSize);
        return ResponseEntity.ok(products);
    }

    @Tag(name = "상품 API", description = "상품 관련 API 목록")
    @Operation(summary = "상품 검색", description = "상품 이름을 검색하여 상품 리스트를 반환합니다. " +
            "검색어가 2글자 미만인 경우 에러를 반환합니다.")
    @GetMapping("/api/v2/products/search")
    public ResponseEntity<List<ProductSummaryDTO>> searchProduct(@RequestParam String searchKeyword) {
        if (searchKeyword.length() < 2) {
            throw new BaseExceptionV2(SEARCH_KEYWORD_ERROR);
        }
        List<ProductSummaryDTO> products = productService.searchProduct(searchKeyword);
        return ResponseEntity.ok(products);
    }

    @Tag(name = "상품 API", description = "상품 관련 API 목록")
    @Operation(summary = "상품 가격 필터링", description = "상품 가격을 필터링합니다. " +
            "최소 가격과 최대 가격을 입력받아 해당하는 상품 리스트를 반환합니다. " +
            "최소 가격이 음수이거나 최대 가격이 음수이거나 최소 가격이 최대 가격보다 큰 경우 에러를 반환합니다.")
    @GetMapping("/api/v2/products/filter/price")
    public ResponseEntity<List<ProductSummaryDTO>> filterProductByPrice(
        @RequestParam int minPrice,
        @RequestParam int maxPrice
    ) {
        if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            throw new BaseExceptionV2(REQUEST_PRICE_ERROR);
        }
        ProductFilter productFilter = new ProductFilter(minPrice, maxPrice);
        List<ProductSummaryDTO> products = productService.filterProducts(productFilter);
        return ResponseEntity.ok(products);
    }

}
