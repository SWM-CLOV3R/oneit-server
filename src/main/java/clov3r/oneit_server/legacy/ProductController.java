package clov3r.oneit_server.legacy;

import clov3r.oneit_server.domain.DTO.ProductDTO;
import clov3r.oneit_server.domain.DTO.ProductDetailDTO;
import clov3r.oneit_server.domain.DTO.ProductSummaryDTO;
import clov3r.oneit_server.domain.entity.Category;
import clov3r.oneit_server.domain.entity.Keyword;
import clov3r.oneit_server.domain.entity.Product;
import clov3r.oneit_server.domain.data.Gender;
import clov3r.oneit_server.repository.ProductRepository;
import clov3r.oneit_server.domain.collection.ProductSearch;
import clov3r.oneit_server.legacy.response.BaseResponse;
import clov3r.oneit_server.service.CategoryService;
import clov3r.oneit_server.service.KeywordService;
import clov3r.oneit_server.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static clov3r.oneit_server.legacy.response.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
public class ProductController {

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
    @PostMapping("/api/v1/product/result/category")
    public BaseResponse<List<ProductDTO>> extractProductsWithCategory(@RequestBody ProductSearch productSearch) {

        // check gender
        if (!Gender.isValid(productSearch.getGender())) {
            return new BaseResponse<>(REQUEST_GENDER_ERROR);
        }
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
    @Operation(
            summary = "성별, 가격, 키워드 필터링 API",
            description = "성별, 가격, 키워드에 해당하는 상품 리스트를 추출합니다. " +
                    "키워드 일치 개수가 많은 순서대로 출력되며 키워드 매칭 개수가 0인 경우 제외됩니다. " +
                    "개수가 5개 미만인 경우 그대로 출력됩니다. ")

    @PostMapping("/api/v1/product/result")
    public BaseResponse<List<ProductDTO>> extractProducts(@RequestBody ProductSearch productSearch) {

//         check gender
        if (!Gender.isValid(productSearch.getGender())) {
            return new BaseResponse<>(REQUEST_GENDER_ERROR);
        }
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
//
//
//    @Tag(name = "선물 추천 API", description = "선물 추천 API 목록")
//    @Operation(
//        summary = "성별, 가격, 키워드, 카테고리 반영한 필터링 API",
//        description = "성별, 가격에 해당하는 상품 리스트 중에서 " +
//            "각 질문에 해당하는 카테고리의 제품 중 키워드를 만족시키는 상품들을 모두 가져온 뒤, " +
//            "그 중 키워드 일치 개수가 많은 순서대로 출력되며 키워드 매칭 개수가 0인 경우 제외됩니다. " +
//            "개수가 5개 미만인 경우 그대로 출력됩니다. ")
//    @GetMapping("/api/v2/products/recommendation")
//    public BaseResponse<List<ProductDTO>> extractProductsWithCategory_v2(@RequestParam ProductSearch productSearch) {
//
//        // check gender
//        if (!Gender.isValid(productSearch.getGender())) {
//            return new BaseResponse<>(REQUEST_GENDER_ERROR);
//        }
//        // check price
//        if (productSearch.getMinPrice() <0 || productSearch.getMaxPrice() < 0 || productSearch.getMinPrice() > productSearch.getMaxPrice()) {
//            return new BaseResponse<>(REQUEST_PRICE_ERROR);
//        }
//        // check keywords
//        if (productSearch.getKeywordsList() == null) {
//            return new BaseResponse<>(REQUEST_ERROR);
//        }
//        List<String> keywords = productSearch.getKeywordsList().stream().map(keyword -> keyword.getKeyword()).toList();
//        for (String keyword : keywords) {
//            System.out.println("keyword = " + keyword);
//        }
//        if (!keywordService.existsByKeyword(keywords)) {
//            return new BaseResponse<>(DATABASE_ERROR_NOT_FOUND);
//        }
//
//        List<Product> products = productService.filterProductsWithCategory(productSearch);
//        // max 5 products
//        if (products.size() > 5) {
//            products = products.subList(0, 5);
//        }
//        List<ProductDTO> productDTOs = products.stream()
//            .map(product -> new ProductDTO(product, keywordService.getKeywordsByIdx(product.getIdx())))
//            .toList();
//
//        return new BaseResponse<>(productDTOs);
//    }


    @Tag(name = "상품 API", description = "상품 관련 API 목록")
    @Operation(summary = "상품 상세 정보 조회")
    @GetMapping("/api/v1/products/{productIdx}")
    public BaseResponse<ProductDetailDTO> getProductDetail(@PathVariable Long productIdx) {
        if (!productRepository.existsProduct(productIdx)) {
            return new BaseResponse<>(PRODUCT_NOT_FOUND);
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
            return new BaseResponse<>(PRODUCT_DTO_ERROR);
        }

        return new BaseResponse<>(productDetailDTO);
    }

    @Tag(name = "상품 API", description = "상품 관련 API 목록")
    @Operation(summary = "상품 리스트 조회 - 페이지네이션", description = "마지막 상품 인덱스와 페이지 사이즈를 입력받아 페이지네이션된 상품 리스트를 반환합니다.  !" +
            "LastproductIdx가 null 일 경우 처음부터 페이지네이션됩니다. " +
            "즉, 첫 페이지에서는 LastProductIdx를 입력하지 않아야 합니다.")
    @GetMapping("/api/v1/products")
    public BaseResponse<List<ProductSummaryDTO>> getProductListPagination(@RequestParam(required = false) Long LastProductIdx, @RequestParam(required = false) Integer pageSize) {

        if (LastProductIdx == null && pageSize == null) {
            return new BaseResponse<>(productService.getAllProducts());
        }
        List<ProductSummaryDTO> products = productService.getProductListPagination(LastProductIdx, pageSize);
        return new BaseResponse<>(products);
    }


    /**
     * 가격대로 상품을 필터링하고 랜덤으로 5개의 상품을 반환합니다.
     * @param productSearch
     * @return
     */
    @Tag(name = "선물 추천 API", description = "선물 추천 API 목록")
    @Operation(hidden = true, summary = "가격 필터링", description = "가격대로 상품을 필터링하고 랜덤으로 5개의 상품을 반환합니다. 결과 제품개수가 5개 미만일 경우 결과 개수 그대로 반환합니다. (키워드 선정하기 전 프론트 연동 테스트용 API 입니다. 성별, 나이는 디폴값으로 요청해도 동작합니다. 가격은 0 <= min < max 이어야 하고, keywords 의 key값은 질문의 번호, 즉 integer 값이어야 합니다.)")
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

}
