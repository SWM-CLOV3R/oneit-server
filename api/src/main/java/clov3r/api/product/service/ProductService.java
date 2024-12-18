package clov3r.api.product.service;

import clov3r.api.auth.repository.UserRepository;
import clov3r.api.product.domain.dto.ProductSummaryDTO;
import clov3r.api.product.domain.collection.ProductFilter;
import clov3r.api.product.domain.collection.ProductSearch;
import clov3r.api.product.repository.KeywordRepository;
import clov3r.api.product.repository.ProductRepository;
import clov3r.api.product.repository.ProductLikeRepository;
import clov3r.domain.domains.entity.Product;
import clov3r.domain.domains.entity.ProductLike;
import clov3r.domain.domains.status.LikeStatus;
import java.util.ArrayList;
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
  private final UserRepository userRepository;


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
    return productRepository.findById(productIdx);
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
          .product(productRepository.findById(productIdx))
          .user(userRepository.findByUserIdx(userIdx))
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

  public List<String> getDetailImages(Long productIdx) {
    Product product = productRepository.findById(productIdx);
    if (product == null) {
      return null;
    }
    String defaultDetailImage = product.getDetailImages();
    if (defaultDetailImage == null || defaultDetailImage.equals("[]")) {
      return null;
    }
    defaultDetailImage = defaultDetailImage.substring(1, defaultDetailImage.length() - 1);
    String[] detailImages = defaultDetailImage.split(",");
    List<String> detailImageList = new ArrayList<>();
    for (String detailImage : detailImages) {
      detailImage = detailImage.trim();
      detailImageList.add(detailImage.trim().substring(1, detailImage.length() - 1));
    }
    return detailImageList;
  }

  public Product getRandomProduct(List<Product> friendWishList, Long excludeProductIdx) {
    Product randomProduct = null;
    if (friendWishList.size() == 1) {
      return friendWishList.get(0);
    }
    while (randomProduct == null) {
      int randomIndex = (int) (Math.random() * friendWishList.size());
      randomProduct = friendWishList.get(randomIndex);
      if (randomProduct.getIdx().equals(excludeProductIdx)) {
        randomProduct = null;
      }
    }
    return randomProduct;
  }

  public List<Product> getRelatedProducts(List<Product> products) {
    List<Long> relatedProductsList = new ArrayList<>();
    for (Product product : products) {
      List<Long> relatedProductIdxList = product.getRelatedProduct();
      relatedProductsList.addAll(relatedProductIdxList);
    }
    relatedProductsList = relatedProductsList.stream().distinct().toList();
    // 랜덤으로 10개만
    if (relatedProductsList.size() > 10) {
      List<Long> randomRelatedProductsList = new ArrayList<>();
      while (randomRelatedProductsList.size() < 10) {
        int randomIndex = (int) (Math.random() * relatedProductsList.size());
        randomRelatedProductsList.add(relatedProductsList.get(randomIndex));
      }
      relatedProductsList = randomRelatedProductsList;
    }
    List<Product> relatedProductsUnion = new ArrayList<>();
    for (Long relatedProductIdx : relatedProductsList) {
      Product relatedProduct = productRepository.findById(relatedProductIdx);
      if (relatedProduct != null) {
        relatedProductsUnion.add(relatedProduct);
      }
    }
    return relatedProductsUnion;
  }
}
