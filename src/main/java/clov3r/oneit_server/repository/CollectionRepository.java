package clov3r.oneit_server.repository;

import static clov3r.oneit_server.domain.entity.QCollection.collection;
import static clov3r.oneit_server.domain.entity.QCollectionProduct.collectionProduct;
import static clov3r.oneit_server.domain.entity.QProduct.product;

import clov3r.oneit_server.domain.data.status.ProductStatus;
import clov3r.oneit_server.domain.data.status.Status;
import clov3r.oneit_server.domain.entity.Collection;
import clov3r.oneit_server.domain.entity.Product;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
              .and(product.status.eq(ProductStatus.ACTIVE))
              .and(collectionProduct.status.eq(Status.ACTIVE)))
          .fetch();

  }


}
