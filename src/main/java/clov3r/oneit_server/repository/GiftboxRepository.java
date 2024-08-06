package clov3r.oneit_server.repository;

import clov3r.oneit_server.domain.DTO.ProductSummaryDTO;
import clov3r.oneit_server.domain.entity.GiftboxProduct;
import clov3r.oneit_server.domain.entity.Product;
import clov3r.oneit_server.domain.request.PostGiftboxRequest;
import clov3r.oneit_server.domain.entity.Giftbox;
import clov3r.oneit_server.domain.entity.GiftboxUser;
import clov3r.oneit_server.domain.entity.User;
import clov3r.oneit_server.response.exception.BaseException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static clov3r.oneit_server.domain.entity.QGiftbox.giftbox;
import static clov3r.oneit_server.domain.entity.QGiftboxProduct.giftboxProduct;
import static clov3r.oneit_server.domain.entity.QGiftboxUser.giftboxUser;
import static clov3r.oneit_server.domain.entity.QProduct.product;
import static clov3r.oneit_server.response.BaseResponseStatus.*;


@Repository
@RequiredArgsConstructor
public class GiftboxRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Transactional
    public Giftbox save(Giftbox giftbox) {
        // giftbox를 생성하고 status를 ACTIVE로 설정, createdAt을 현재 시간으로 설정
        em.persist(giftbox);
        em.flush();
        return giftbox;

    }

    @Transactional
    public void updateImageUrl(Long idx, String imageUrl) {
        // giftbox의 imageUrl을 수정
        // with QueryDSL
        queryFactory.update(giftbox)
                .set(giftbox.imageUrl, imageUrl)
                .where(giftbox.idx.eq(idx))
                .execute();

    }

    @Transactional
    public Giftbox findById(Long giftboxIdx) throws BaseException {
        // idx로 status가 ACTIVE인 row만 조회
        Giftbox result = queryFactory.select(giftbox)
                .from(giftbox)
                .where(giftbox.idx.eq(giftboxIdx)
                        .and(giftbox.status.eq("ACTIVE")))
                .fetchOne();
        return result;
    }

    @Transactional
    public void deleteById(Long giftboxIdx) {
        // status가 ACTIVE인 row일 경우에만 DELETED로 변경, deletedAt을 현재 시간으로 변경
        queryFactory.update(giftbox)
                .set(giftbox.status, "DELETED")
                .set(giftbox.deletedAt, LocalDateTime.now())
                .where(giftbox.idx.eq(giftboxIdx),
                        giftbox.status.eq("ACTIVE"))
                .execute();
    }

    @Transactional
    public void updateGiftbox(Long giftboxIdx, PostGiftboxRequest request) {
        // giftbox의 정보를 수정
        queryFactory.update(giftbox)
                .set(giftbox.name, request.getName())
                .set(giftbox.description, request.getDescription())
                .set(giftbox.deadline, request.getDeadline())
                .set(giftbox.accessStatus, request.getAccessStatus())
                .set(giftbox.updatedAt, LocalDateTime.now())
                .where(giftbox.idx.eq(giftboxIdx),
                        giftbox.status.eq("ACTIVE"))
                .execute();
    }

    @Transactional
    public void createGiftboxManager(Long createdUserIdx, Long idx) {

        GiftboxUser newGiftboxUser;
        try {
            newGiftboxUser = new GiftboxUser(
                em.find(Giftbox.class, idx),
                em.find(User.class, createdUserIdx),
                "MANAGER");
            newGiftboxUser.createBaseEntity();
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR_NOT_FOUND);
        }
        // giftbox와 user를 연결
        em.persist(newGiftboxUser);
    }

    @Transactional
    public void addProductToGiftbox(Long giftboxIdx, Long productIdx) {
        GiftboxProduct deletedProduct = queryFactory.select(giftboxProduct)
                .from(giftboxProduct)
                .where(giftboxProduct.giftbox.idx.eq(giftboxIdx),
                        giftboxProduct.product.idx.eq(productIdx),
                        giftboxProduct.status.eq("DELETED"))
                .fetchOne();
        if (deletedProduct != null) {
            // status가 DELETED인 giftboxProduct가 존재할 경우 status를 ACTIVE로 변경
            queryFactory.update(giftboxProduct)
                    .set(giftboxProduct.status, "ACTIVE")
                    .set(giftboxProduct.updatedAt, LocalDateTime.now())
                    .where(giftboxProduct.giftbox.idx.eq(giftboxIdx),
                            giftboxProduct.product.idx.eq(productIdx),
                            giftboxProduct.status.eq("DELETED"))
                    .execute();
            return;
        } else {
            // giftbox와 product를 연결
            GiftboxProduct giftboxProduct = new GiftboxProduct(
                em.find(Giftbox.class, giftboxIdx),
                em.find(Product.class, productIdx)
            );
            giftboxProduct.createBaseEntity();
            em.persist(giftboxProduct);
        }

    }

    public List<Giftbox> findAll() {
        // status가 ACTIVE인 모든 giftbox 조회
        List<Giftbox> giftboxList = queryFactory.select(giftbox)
                .from(giftbox)
                .where(giftbox.status.eq("ACTIVE"))
                .fetch();
        return giftboxList;
    }

    public List<Giftbox> findGiftboxOfUser(Long userIdx) {
        // userIdx로 status가 ACTIVE인 giftbox 조회
        return queryFactory.select(giftbox)
                .from(giftbox)
                .join(giftbox.participants, giftboxUser)
                .where(giftboxUser.user.idx.eq(userIdx),
                        giftboxUser.status.eq("ACTIVE"),
                        giftbox.status.eq("ACTIVE"))
                .fetch();
    }


    public List<Product> findProductOfGiftbox(Long giftboxIdx) {
        // giftboxIdx로 status가 ACTIVE인 product 조회
        return queryFactory.select(product)
                .from(giftbox)
                .join(giftbox.products, giftboxProduct)
                .where(giftboxProduct.giftbox.idx.eq(giftboxIdx),
                        product.idx.eq(giftboxProduct.product.idx),
                        product.status.eq("ACTIVE"),
                        giftboxProduct.status.eq("ACTIVE"))
                .fetch();
    }

    @Transactional
    public void deleteProductOfGiftbox(Long giftboxIdx, Long productIdx) {
        queryFactory.update(giftboxProduct)
                .set(giftboxProduct.status, "DELETED")
                .set(giftboxProduct.deletedAt, LocalDateTime.now())
                .where(giftboxProduct.giftbox.idx.eq(giftboxIdx),
                        giftboxProduct.product.idx.eq(productIdx),
                        giftboxProduct.status.eq("ACTIVE"))
                .execute();
    }
}