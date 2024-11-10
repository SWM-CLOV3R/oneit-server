package clov3r.api.product.repository;

import static clov3r.domain.domains.entity.QCollection.collection;
import static clov3r.domain.domains.entity.QCollectionProduct.collectionProduct;
import static clov3r.domain.domains.entity.QProduct.product;

import clov3r.api.product.domain.status.ProductStatus;
import clov3r.domain.domains.entity.Collection;
import clov3r.domain.domains.entity.CollectionProduct;
import clov3r.domain.domains.entity.Product;
import clov3r.domain.domains.status.Status;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CollectionRepository {
  private final EntityManager em;
  private final JPAQueryFactory queryFactory;

  public Collection getCollection(Long collectionIdx) {
    return queryFactory.select(collection)
        .from(collection)
        .where(collection.idx.eq(collectionIdx)
            .and(collection.status.eq(Status.ACTIVE)))
        .fetchOne();
  }

  public List<Collection> getCollectionList() {
    return queryFactory.select(collection)
        .from(collection)
        .where(collection.status.eq(Status.ACTIVE))
        .fetch();
  }

  public List<Product> getProductList(Long collectionIdx) {
      // select p.* from product p join collection_product cp on p.idx = cp.product_idx where cp.collection_idx = :collectionIdx
      return queryFactory.select(product)
          .from(product)
          .join(collectionProduct)
          .on(product.idx.eq(collectionProduct.product.idx))
          .where(collectionProduct.collection.idx.eq(collectionIdx)
              .and(product.status.eq(ProductStatus.ACTIVE)))
          .fetch();

  }

  public List<CollectionProduct> getCollectionProductList(Long collectionIdx) {
    return queryFactory.select(collectionProduct)
        .from(collectionProduct)
        .where(collectionProduct.collection.idx.eq(collectionIdx)
            .and(collectionProduct.status.eq(Status.ACTIVE))
            .and(collectionProduct.product.status.eq(ProductStatus.ACTIVE)))
        .fetch();
  }

}
