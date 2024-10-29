package clov3r.api.common.service;

import clov3r.api.common.domain.DTO.ProductSummaryDTO;
import clov3r.api.common.domain.collection.ProductFilter;
import clov3r.api.common.domain.collection.ProductSearch;
import clov3r.api.common.domain.data.status.LikeStatus;
import clov3r.api.common.domain.entity.Product;
import clov3r.api.common.domain.entity.ProductLike;
import clov3r.api.common.repository.KeywordRepository;
import clov3r.api.common.repository.ProductRepository;
import clov3r.api.common.repository.ProductLikeRepository;
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
  private final ProductLikeRepository productLikeRepository;


  public List<ProductSummaryDTO> filterProducts(ProductFilter productFilter, Long userIdx) {
    // First Query: Filter by price and gender
    List<Product> products = productRepository.filterProductsByPrice(productFilter);
    return products.stream()
        .map(product -> new ProductSummaryDTO(
            product,
            getLikeStatus(product.getIdx(), userIdx)
        )).toList();

//        // Second Query: Further filter by keywords if applicable
//        if (!productSearch.getKeywords().isEmpty()) {
//            return productRepository.filterProductsByKeywords(initialFilteredProducts, productSearch.getKeywords());
//        }

  }

  public Product getProductByIdx(Long productIdx) {
    Product product = productRepository.findById(productIdx);
//        System.out.println("product.toString() = " + product.toString());
    return product;
  }

  public List<Product> filterProductsWithCategory(ProductSearch productSearch) {

    List<Product> initialFilteredProducts = productRepository.filterProductsByPriceAndGender(
        productSearch);

    // filter by category and answer keywords
    List<Product> filterProductsByCategoryKeywords = productRepository.filterProductsByCategoryAndKeywords(
        initialFilteredProducts, productSearch);

    if (!productSearch.getKeywords().isEmpty()) {
      return productRepository.filterProductsByKeywords(filterProductsByCategoryKeywords,
          productSearch.getKeywords());
    }

    return initialFilteredProducts;
  }

  public List<ProductSummaryDTO> getAllProducts(Long userIdx) {
    List<Product> products = productRepository.findAll();
    return products.stream()
        .map(product ->
            new ProductSummaryDTO(
                product,
                getLikeStatus(product.getIdx(), userIdx)
            )).toList();
  }

  public List<ProductSummaryDTO> getProductListPagination(Long lastProductIdx, int pageSize, Long userIdx) {
    List<Product> products = productRepository.findProductListPagination(lastProductIdx, pageSize);
    return products.stream()
        .map(product ->
            new ProductSummaryDTO(
                product,
                getLikeStatus(product.getIdx(), userIdx)
            )).toList();
  }

  public List<ProductSummaryDTO> searchProduct(String searchKeyword, Long userIdx) {
    List<Product> products = productRepository.searchProduct(searchKeyword);
    return products.stream()
        .map(product -> new ProductSummaryDTO(
            product,
            getLikeStatus(product.getIdx(), userIdx)
        )).toList();
  }

  @Transactional
  public void likeProduct(Long productIdx, Long userIdx) {
    ProductLike productLike = productLikeRepository.findProductLike(productIdx, userIdx);
    if (productLike != null) {
      if (productLike.getLikeStatus().equals(LikeStatus.LIKE)) {
        productLike.setLikeStatus(LikeStatus.NONE);
      } else {
        productLike.setLikeStatus(LikeStatus.LIKE);
      }
    } else {
      ProductLike newProductLike = ProductLike.builder()
          .productIdx(productIdx)
          .userIdx(userIdx)
          .likeStatus(LikeStatus.LIKE)
          .build();
      newProductLike.createBaseEntity();
      productLikeRepository.save(newProductLike);
    }
    updateProductLikeCount(productIdx);
  }

  public int getLikeCount(Long productIdx) {
    return productLikeRepository.countByProductIdxAndLikeStatus(productIdx, LikeStatus.LIKE);
  }

  public void updateProductLikeCount(Long productIdx) {
    productRepository.updateProductLikeCount(productIdx, getLikeCount(productIdx));
  }

  public LikeStatus getLikeStatus(Long productIdx, Long userIdx) {
    if (userIdx == null) {
      return LikeStatus.NONE;
    }
    ProductLike productLike = productLikeRepository.findProductLike(productIdx, userIdx);
    if (productLike == null) {
      return LikeStatus.NONE;
    }
    return productLike.getLikeStatus();
  }

}
