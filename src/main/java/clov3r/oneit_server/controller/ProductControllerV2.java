package clov3r.oneit_server.controller;

import static clov3r.oneit_server.error.errorcode.CommonErrorCode.*;
import static clov3r.oneit_server.error.errorcode.CommonErrorCode.DATABASE_ERROR_NOT_FOUND;
import static clov3r.oneit_server.error.errorcode.CustomErrorCode.*;

import clov3r.oneit_server.domain.DTO.ProductDTO;
import clov3r.oneit_server.domain.DTO.ProductDetailDTO;
import clov3r.oneit_server.domain.DTO.ProductSummaryDTO;
import clov3r.oneit_server.domain.collection.ProductSearch;
import clov3r.oneit_server.domain.data.Gender;
import clov3r.oneit_server.domain.entity.Category;
import clov3r.oneit_server.domain.entity.Keyword;
import clov3r.oneit_server.domain.entity.Product;
import clov3r.oneit_server.error.exception.BaseExceptionV2;
import clov3r.oneit_server.repository.ProductRepository;
import clov3r.oneit_server.service.CategoryService;
import clov3r.oneit_server.service.KeywordService;
import clov3r.oneit_server.service.ProductService;
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
    public ResponseEntity<List<ProductDTO>> extractProductsWithCategory(@RequestBody ProductSearch productSearch) {

        // check gender
        System.out.println("productSearch = " + productSearch);
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
    public ResponseEntity<List<ProductSummaryDTO>> getProductListPagination(@RequestParam(required = false) Long LastProductIdx, @RequestParam(required = false) Integer pageSize) {

        if (LastProductIdx == null && pageSize == null) {
            return ResponseEntity.ok(productService.getAllProducts());
        }
        List<ProductSummaryDTO> products = productService.getProductListPagination(LastProductIdx, pageSize);
        return ResponseEntity.ok(products);
    }


}