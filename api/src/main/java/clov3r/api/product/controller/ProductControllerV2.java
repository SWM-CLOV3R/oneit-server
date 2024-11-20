package clov3r.api.product.controller;

import clov3r.api.product.domain.dto.ProductDTO;
import clov3r.api.common.error.errorcode.CommonErrorCode;
import clov3r.api.common.error.errorcode.CustomErrorCode;
import clov3r.api.common.error.exception.BaseExceptionV2;
import clov3r.api.auth.security.Auth;
import clov3r.api.product.domain.dto.ProductDetailDTO;
import clov3r.api.product.domain.dto.ProductRecommandDTO;
import clov3r.api.product.domain.dto.ProductSummaryDTO;
import clov3r.api.product.domain.collection.ProductFilter;
import clov3r.api.product.domain.collection.ProductSearch;
import clov3r.api.product.repository.ProductLikeRepository;
import clov3r.api.product.repository.ProductRepository;
import clov3r.api.product.service.CategoryService;
import clov3r.api.product.service.KeywordService;
import clov3r.api.product.service.ProductService;
import clov3r.domain.domains.entity.Product;
import clov3r.domain.domains.status.LikeStatus;
import clov3r.domain.domains.type.Gender;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    private final ProductLikeRepository productLikeRepository;


    @Tag(name = "선물 추천 API", description = "선물 추천 API 목록")
    @Operation(
            summary = "성별, 가격, 키워드, 카테고리 반영한 필터링 API",
            description = "성별, 가격에 해당하는 상품 리스트 중에서 " +
                    "각 질문에 해당하는 카테고리의 제품 중 키워드를 만족시키는 상품들을 모두 가져온 뒤, " +
                    "그 중 키워드 일치 개수가 많은 순서대로 출력되며 키워드 매칭 개수가 0인 경우 제외됩니다. " +
                    "개수가 5개 미만인 경우 그대로 출력됩니다. ")
    @PostMapping("/api/v2/product/result/category")
    public ResponseEntity<List<ProductDTO>> extractProductsWithCategory(
        @RequestBody ProductSearch productSearch,
        @Parameter(hidden = true) @Auth(required = false) Long userIdx
    ) {

        // check gender
        if (!Gender.isValid(productSearch.getGender())) {
            log.info(CustomErrorCode.REQUEST_GENDER_ERROR.getMessage());
            throw new BaseExceptionV2(CustomErrorCode.REQUEST_GENDER_ERROR);
        }
        // check price
        if (productSearch.getMinPrice() < 0 || productSearch.getMaxPrice() < 0 || productSearch.getMinPrice() > productSearch.getMaxPrice()) {
            throw new BaseExceptionV2(CustomErrorCode.REQUEST_PRICE_ERROR);
        }
        // check keywords
        if (productSearch.getKeywords() == null) {
            throw new BaseExceptionV2(CommonErrorCode.REQUEST_ERROR);
        }
        if (!keywordService.existsByKeyword(productSearch.getKeywords().values().stream().toList())) {
            throw new BaseExceptionV2(CommonErrorCode.DATABASE_ERROR_NOT_FOUND);
        }

        List<Product> products = productService.filterProductsWithCategory(productSearch);
        // max 5 products
        if (products.size() > 5) {
            products = products.subList(0, 5);
        }
        List<ProductDTO> productDTOs = products.stream()
                .map(product ->
                    new ProductDTO(
                        product,
                        productService.getLikeStatus(product.getIdx(), userIdx)
                    ))
                .toList();

        if (productDTOs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(productDTOs);
    }

//    @GetMapping("/api/v2/product/recommand/result")
//    public ResponseEntity<ProductRecommandDTO> recommandProductsWithCategory(
//        @ModelAttribute ProductSearch productSearch,
//        @Parameter(hidden = true) @Auth(required = false) Long userIdx
//    ) {
//    }

    @Tag(name = "선물 추천 API", description = "선물 추천 API 목록")
    @Operation(
        summary = "성별, 가격, 키워드, 카테고리 반영한 필터링 API + 관련 상품 조회",
        description = "성별, 가격에 해당하는 상품 리스트 중에서 " +
            "각 질문에 해당하는 카테고리의 제품 중 키워드를 만족시키는 상품들을 모두 가져온 뒤, " +
            "그 중 키워드 일치 개수가 많은 순서대로 출력되며 키워드 매칭 개수가 0인 경우 제외됩니다. " +
            "개수가 5개 미만인 경우 그대로 출력됩니다. ")
    @PostMapping("/api/v2/product/recommand/result")
    public ResponseEntity<ProductRecommandDTO> recommandProductsWithCategory(
        @RequestBody ProductSearch productSearch,
        @Parameter(hidden = true) @Auth(required = false) Long userIdx
    ) {
        // check gender
        if (!Gender.isValid(productSearch.getGender())) {
            log.info(CustomErrorCode.REQUEST_GENDER_ERROR.getMessage());
            throw new BaseExceptionV2(CustomErrorCode.REQUEST_GENDER_ERROR);
        }
        // check price
        if (productSearch.getMinPrice() < 0 || productSearch.getMaxPrice() < 0 || productSearch.getMinPrice() > productSearch.getMaxPrice()) {
            throw new BaseExceptionV2(CustomErrorCode.REQUEST_PRICE_ERROR);
        }
        // check keywords
        if (productSearch.getKeywords() == null) {
            throw new BaseExceptionV2(CommonErrorCode.REQUEST_ERROR);
        }
        if (!keywordService.existsByKeyword(productSearch.getKeywords().values().stream().toList())) {
            throw new BaseExceptionV2(CommonErrorCode.DATABASE_ERROR_NOT_FOUND);
        }

        List<Product> products = productService.filterProductsWithCategory(productSearch);
        // max 5 products
        if (products.size() > 5) {
            products = products.subList(0, 5);
        }
        List<ProductDTO> productDTOs = products.stream()
            .map(product ->
                new ProductDTO(
                    product,
                    productService.getLikeStatus(product.getIdx(), userIdx)
                ))
            .toList();

        if (productDTOs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<Product> relatedProducts = productService.getRelatedProducts(products);
        List<ProductDTO> relatedProductDTOs = relatedProducts.stream()
            .map(product ->
                new ProductDTO(
                    product,
                    productService.getLikeStatus(product.getIdx(), userIdx)
                ))
            .toList();
        ProductRecommandDTO productRecommandDTO = new ProductRecommandDTO(
            productDTOs,
            relatedProductDTOs
        );

        return ResponseEntity.ok(productRecommandDTO);
    }




    @Tag(name = "상품 API", description = "상품 관련 API 목록")
    @Operation(summary = "상품 상세 정보 조회")
    @GetMapping("/api/v2/products/{productIdx}")
    public ResponseEntity<ProductDetailDTO> getProductDetail(
        @PathVariable Long productIdx,
        @Parameter(hidden = true) @Auth(required = false) Long userIdx
    ) {
        Product product = productService.getProductByIdx(productIdx);
        if (product == null) {
            throw new BaseExceptionV2(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
        ProductDetailDTO productDetailDTO = new ProductDetailDTO(
            product,
            productService.getLikeStatus(productIdx, userIdx),
            productService.getDetailImages(productIdx)
        );
        return ResponseEntity.ok(productDetailDTO);
    }

    @Tag(name = "상품 API", description = "상품 관련 API 목록")
    @Operation(summary = "상품 리스트 조회 - 페이지네이션", description = "마지막 상품 인덱스와 페이지 사이즈를 입력받아 페이지네이션된 상품 리스트를 반환합니다." +
            "LastproductIdx가 null 일 경우 처음부터 페이지네이션됩니다. " +
            "즉, 첫 페이지에서는 LastProductIdx를 입력하지 않아야 합니다.")
    @GetMapping("/api/v2/products")
    public ResponseEntity<List<ProductSummaryDTO>> getProductListPagination(
        @RequestParam(required = false) Long LastProductIdx,
        @RequestParam(required = false) Integer pageSize,
        @Parameter(hidden = true) @Auth(required = false) Long userIdx
    ) {

        if (LastProductIdx == null && pageSize == null) {
            return ResponseEntity.ok(productService.getAllProducts(userIdx));
        }
        List<ProductSummaryDTO> products = productService.getProductListPagination(LastProductIdx, pageSize, userIdx);
        return ResponseEntity.ok(products);
    }

    @Tag(name = "상품 API", description = "상품 관련 API 목록")
    @Operation(summary = "상품 검색", description = "상품 이름을 검색하여 상품 리스트를 반환합니다. " +
            "검색어가 2글자 미만인 경우 에러를 반환합니다.")
    @GetMapping("/api/v2/products/search")
    public ResponseEntity<List<ProductSummaryDTO>> searchProduct(
        @RequestParam String searchKeyword,
        @Parameter(hidden = true) @Auth(required = false) Long userIdx
    ) {
        if (searchKeyword.length() < 2) {
            throw new BaseExceptionV2(CustomErrorCode.SEARCH_KEYWORD_ERROR);
        }
        List<ProductSummaryDTO> products = productService.searchProduct(searchKeyword, userIdx);
        return ResponseEntity.ok(products);
    }

    @Tag(name = "상품 API", description = "상품 관련 API 목록")
    @Operation(summary = "상품 가격 필터링", description = "상품 가격을 필터링합니다. " +
            "최소 가격과 최대 가격을 입력받아 해당하는 상품 리스트를 반환합니다. " +
            "최소 가격이 음수이거나 최대 가격이 음수이거나 최소 가격이 최대 가격보다 큰 경우 에러를 반환합니다.")
    @GetMapping("/api/v2/products/filter/price")
    public ResponseEntity<List<ProductSummaryDTO>> filterProductByPrice(
        @RequestParam int minPrice,
        @RequestParam int maxPrice,
        @Parameter(hidden = true) @Auth(required = false) Long userIdx
    ) {
        if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            throw new BaseExceptionV2(CustomErrorCode.REQUEST_PRICE_ERROR);
        }
        ProductFilter productFilter = new ProductFilter(minPrice, maxPrice);
        List<ProductSummaryDTO> products = productService.filterProducts(productFilter, userIdx);
        return ResponseEntity.ok(products);
    }

    @Tag(name = "상품 API", description = "상품 관련 API 목록")
    @Operation(summary = "상품 좋아요", description = "상품 좋아요를 누릅니다.")
    @PostMapping("/api/v2/products/{productIdx}/like")
    public ResponseEntity<ProductSummaryDTO> likeProduct(
        @PathVariable Long productIdx,
        @Parameter(hidden = true) @Auth Long userIdx
    ) {
        if (!productRepository.existsProduct(productIdx)) {
            throw new BaseExceptionV2(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
        productService.likeProduct(productIdx, userIdx);
        Product product = productService.getProductByIdx(productIdx);
        ProductSummaryDTO productSummaryDTO = new ProductSummaryDTO(
            product,
            productService.getLikeStatus(productIdx, userIdx)
        );
        return ResponseEntity.ok(productSummaryDTO);
    }

    @Tag(name = "상품 API", description = "상품 관련 API 목록")
    @Operation(summary = "좋아요 상품 리스트 조회", description = "유저별로 좋아요를 누른 상품 리스트를 조회합니다.")
    @GetMapping("/api/v2/products/like")
    public ResponseEntity<List<ProductSummaryDTO>> getLikeProductList(
        @Parameter(hidden = true) @Auth Long userIdx
    ) {
        List<Product> products = productLikeRepository.findLikeProductList(userIdx, LikeStatus.LIKE);
        List<ProductSummaryDTO> productSummaryDTOS = products.stream()
            .map(product -> new ProductSummaryDTO(
                product,
                productService.getLikeStatus(product.getIdx(), userIdx)
            )).toList();
        return ResponseEntity.ok(productSummaryDTOS);
    }
}
