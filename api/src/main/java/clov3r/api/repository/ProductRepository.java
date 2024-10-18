package clov3r.api.repository;

import static clov3r.api.domain.entity.QProduct.product;

import clov3r.api.domain.DTO.ProductSummaryDTO;
import clov3r.api.domain.collection.MatchedProduct;
import clov3r.api.domain.collection.ProductSearch;
import clov3r.api.domain.collection.QuestionCategory;
import clov3r.api.domain.data.Gender;
import clov3r.api.domain.entity.Product;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final EntityManager em;
    private final KeywordRepository keywordRepository;
    private final JPAQueryFactory jpaQueryFactory;

    // save Product
    /**
     * 가격과 성별을 기준으로 상품을 필터링해서 해당하는 상품 리스트를 반환합니다.
     * @param productSearch
     * @return List<Product>
     */
    public List<Product> filterProductsByPriceAndGender(ProductSearch productSearch) {
        // First Query: Filter by price and gender
        String jpql = "select distinct p from Product p where p.originalPrice > :minPrice and p.originalPrice < :maxPrice and p.status = 'ACTIVE'";


        if (!productSearch.getGender().equals(Gender.UNISEX)) {
            jpql += " and p.gender <> :excludedGender";
        }

        TypedQuery<Product> query = em.createQuery(jpql, Product.class);
        query.setParameter("minPrice", productSearch.getMinPrice());
        query.setParameter("maxPrice", productSearch.getMaxPrice());

        if (!productSearch.getGender().equals(Gender.UNISEX.toString())) {
            Gender excludedGender = productSearch.getGender().equals(Gender.MALE) ? Gender.FEMALE : Gender.MALE;
            query.setParameter("excludedGender", excludedGender);
        }

        List<Product> resultList = query.getResultList();
        Set<Product> uniqueProducts = new HashSet<>(resultList);
        // Convert the Set back to a List if needed
        List<Product> uniqueProductList = new ArrayList<>(uniqueProducts);
        return uniqueProductList;
    }

    /**
     * 가격을 기준으로 상품을 필터링해서 해당하는 상품 리스트를 반환합니다.
     * @param productSearch
     * @return List<Product>
     */
    public List<Product> filterProductsByPrice(ProductSearch productSearch) {
        // Filter by price
        String jpql = "select p from Product p where p.originalPrice > :minPrice and p.originalPrice < :maxPrice";
        TypedQuery<Product> query = em.createQuery(jpql, Product.class);
        query.setParameter("minPrice", productSearch.getMinPrice());
        query.setParameter("maxPrice", productSearch.getMaxPrice());

        return query.getResultList();
    }

    /**
     * 키워드를 기준으로 상품을 필터링해서 해당하는 상품 리스트를 반환합니다.
     * @param products
     * @param keywords
     * @return
     */
    public List<Product> filterProductsByKeywords(List<Product> products, HashMap<Integer, String> keywords) {
        if (products.isEmpty() || keywords.isEmpty()) {
            return products;
        }

        // make question category object
        List<QuestionCategory> questionCategories = createQuestionCategory();
        List<MatchedProduct> matchedProductList = new ArrayList<>();

        // product idx list and keyword idx list
        List<Long> productIdxList = products.stream().map(Product::getIdx).toList();
        List<Long> keywordIdxList = keywordRepository.getKeywordIdxList(keywords.values().stream().toList());

        // if match product keyword, add 1 scores
        // count matched keywords with keywordList per products
        for (Long productIdx: productIdxList) {
            int matchedKeywordCount = 0;
            List<Long> productKeywordIdxList = keywordRepository.getProductKeywordIdxList(productIdx);
            for (Long keywordIdx : keywordIdxList) {
                if (productKeywordIdxList.contains(keywordIdx)) {
                    matchedKeywordCount++;
                }
            }
            matchedProductList.add(new MatchedProduct(productIdx, matchedKeywordCount));
        }

        // order by matched keyword count
        matchedProductList.sort((o1, o2) -> o2.getMatchedKeywordCount() - o1.getMatchedKeywordCount());
        // delete 0 matched keyword count
        matchedProductList.removeIf(matchedProduct -> matchedProduct.getMatchedKeywordCount() == 0);

        // convert to product List from matchedProduct List
        List<Long> matchedProductIdxList = matchedProductList.stream().map(MatchedProduct::getIdx).toList();
        // keep order matchedProductsIdxList when finding products List from database
        List<Product> matchedProducts = findProductsByIdxOrdered(matchedProductIdxList);
        return matchedProducts;
    }

    public List<Product> filterProductsByCategoryAndKeywords(List<Product> initialFilteredProducts, ProductSearch productSearch) {
        HashMap<Integer, String> questionKeywords = productSearch.getKeywords();
        if (initialFilteredProducts.isEmpty() || questionKeywords.isEmpty()) {
            return initialFilteredProducts;
        }

        List<Product> AllProductsFilterdByCategory= new ArrayList<>();

        // make question category object
        List<QuestionCategory> questionCategories = createQuestionCategory();
        for (QuestionCategory q: questionCategories) {
            // 해당 질문과 관련된 카테고리 idx 리스트
            List<Long> categoryIdxList = q.getCategoryIdxList();
            // 해당 질문에 대한 사용자의 답변 키워드
            String answerKeyword = questionKeywords.get(q.getQuestionIdx());
            // 해당 질문에 대한 사용자의 답변 키워드 idx
            Long answerKeywordIdx = keywordRepository.findKeywordIdx(answerKeyword);

            // 해당 카테고리(대분류)이면서 해당 키워드를 가진 제품 리스트
            String jpql = "select p from Product p " +
                    "join ProductKeyword pk on p.idx = pk.product.idx " +
                    "where pk.keyword.idx = :keywordIdx " +
                    "and p.idx in :initialFilteredProducts " +
                    "and p.category.idx in :categoryIdxList";
            TypedQuery<Product> query = em.createQuery(jpql, Product.class);
            query.setParameter("keywordIdx", answerKeywordIdx);
            query.setParameter("initialFilteredProducts", initialFilteredProducts.stream().map(Product::getIdx).toList());
            query.setParameter("categoryIdxList", categoryIdxList);
            List<Product> productsFilteredByCategory = new ArrayList<>(query.getResultList());
            AllProductsFilterdByCategory.addAll(productsFilteredByCategory);
            Set<Product> uniqueProducts = new HashSet<>(AllProductsFilterdByCategory);
            // Convert the Set back to a List if needed
            AllProductsFilterdByCategory = new ArrayList<>(uniqueProducts);
            AllProductsFilterdByCategory.sort(Comparator.comparing(Product::getIdx));
        }

        return AllProductsFilterdByCategory;
    }

    private Product findProductByIdx(Long productIdx) {
        return em.find(Product.class, productIdx);
    }

    public List<Product> findProductsByIdxOrdered(List<Long> productIdxList) {
        // Fetch products from the database
        List<Product> unorderedProducts = em.createQuery("select p from Product p where p.idx in :productIdxList", Product.class)
                .setParameter("productIdxList", productIdxList)
                .getResultList();

        // Create a map for quick ID to Product lookup
        Map<Long, Product> productMap = unorderedProducts.stream()
                .collect(Collectors.toMap(Product::getIdx, product -> product));

        // Order products based on the order of IDs in matchedProductIdxList
        List<Product> orderedProducts = productIdxList.stream()
                .map(productMap::get)
                .collect(Collectors.toList());

        return orderedProducts;
    }

    public List<Product> findProductListByIdxList(List<Long> productIdxList) {
        return em.createQuery("select p from Product p where p.idx in :productIdxList", Product.class)
                .setParameter("productIdxList", productIdxList)
                .getResultList();
    }


    private List<QuestionCategory> createQuestionCategory() {
        List<QuestionCategory> questionCategoryList = new ArrayList<>();

        for(int i = 0; i < 8; i++) {
            QuestionCategory q = new QuestionCategory(i);
            questionCategoryList.add(q);

            if (i == 3 || i == 5) {
                q.getCategoryIdxList().add(1L);  // 가구/인테리어
                q.getCategoryIdxList().add(7L);  // 패션의류
                q.getCategoryIdxList().add(8L);  // 패션잡화
            } else if (i == 6 || i == 1) {
                q.getCategoryIdxList().add(2L);  // 디지털/가전
                q.getCategoryIdxList().add(3L);  // 생활/건강
                q.getCategoryIdxList().add(6L);  // 출산/육아
            } else if (i == 4 || i == 0) {
                q.getCategoryIdxList().add(5L);  // 식품
            } else {
                q.getCategoryIdxList().add(9L);  // 화장품/미용
            }
        }

        return questionCategoryList;
    }

    private String buildInitialQuery(ProductSearch productSearch) {
        String jpql = "select p from Product p where p.originalPrice > :minPrice and p.originalPrice < :maxPrice";
        if (!productSearch.getGender().equals(Gender.UNISEX.toString())) {
            jpql += " and p.gender <> :excludedGender";
        }
        return jpql;
    }

    public Product findById(Long productIdx) {
        return em.find(Product.class, productIdx);
    }

    private void setInitialQueryParameters(TypedQuery<Product> query, ProductSearch productSearch) {
        query.setParameter("minPrice", productSearch.getMinPrice());
        query.setParameter("maxPrice", productSearch.getMaxPrice());
        if (!productSearch.getGender().equals(Gender.UNISEX.toString())) {
            String excludedGender = productSearch.getGender().equals(Gender.MALE.toString()) ? Gender.FEMALE.toString() : Gender.MALE.toString();
            query.setParameter("excludedGender", excludedGender);
        }
    }


    /**
     * NoOffset 방식으로 상품 리스트를 반환합니다.
     * @param productIdx
     * @param pageSize
     * @return
     */
    public List<ProductSummaryDTO> findProductListPagination(Long productIdx, int pageSize) {
        return jpaQueryFactory
                .select(
                    Projections.fields(ProductSummaryDTO.class,
                        product.idx,
                        product.name,
                        product.originalPrice,
                        product.currentPrice,
                        product.discountRate,
                        product.thumbnailUrl))
                .from(product)
                .where(ltProduct(productIdx))
                .orderBy(product.idx.desc())
                .limit(pageSize)
                .fetch();
    }

    /**
     * productIdx 보다 작은 상품 리스트를 반환합니다.
     * @param productIdx
     * @return
     */
    private BooleanExpression ltProduct(Long productIdx) {
        if (productIdx == null) {
            return null;
        }
        return product.idx.lt(productIdx);
    }
    public List<Product> findAll() {
        return em.createQuery("select p from Product p", Product.class)
                .getResultList();
    }

    public boolean existsProduct(Long productIdx) {
        // productIdx로 status가 ACTIVE인 product가 존재하는지 확인
        return jpaQueryFactory.selectOne()
                .from(product)
                .where(product.idx.eq(productIdx))
                .fetchFirst() != null;

    }

    public List<Product> searchProduct(String searchKeyword) {
        return jpaQueryFactory.selectFrom(product)
                .where(product.name.contains(searchKeyword)
                        .or(product.brandName.contains(searchKeyword)))
                .fetch();
    }

}

