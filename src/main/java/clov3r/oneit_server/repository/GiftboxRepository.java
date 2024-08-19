package clov3r.oneit_server.repository;

import clov3r.oneit_server.domain.data.GiftboxUserRole;
import clov3r.oneit_server.domain.data.status.InvitationStatus;
import clov3r.oneit_server.domain.data.status.ProductStatus;
import clov3r.oneit_server.domain.data.status.Status;
import clov3r.oneit_server.domain.entity.GiftboxProduct;
import clov3r.oneit_server.domain.entity.Product;
import clov3r.oneit_server.domain.request.PostGiftboxRequest;
import clov3r.oneit_server.domain.entity.Giftbox;
import clov3r.oneit_server.domain.entity.GiftboxUser;
import clov3r.oneit_server.domain.entity.User;
import clov3r.oneit_server.exception.BaseException;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
    public Giftbox findById(Long giftboxIdx) {
        // idx로 status가 ACTIVE인 row만 조회
        Giftbox result = queryFactory.select(giftbox)
                .from(giftbox)
                .where(giftbox.idx.eq(giftboxIdx)
                        .and(giftbox.status.eq(Status.ACTIVE)))
                .fetchOne();
        return result;
    }

    @Transactional
    public void deleteById(Long giftboxIdx) {
        // status가 ACTIVE인 row일 경우에만 DELETED로 변경, deletedAt을 현재 시간으로 변경
        queryFactory.update(giftbox)
                .set(giftbox.status, Status.DELETED)
                .set(giftbox.deletedAt, LocalDateTime.now())
                .where(giftbox.idx.eq(giftboxIdx),
                        giftbox.status.eq(Status.ACTIVE))
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
                        giftbox.status.eq(Status.ACTIVE))
                .execute();
    }

    @Transactional
    public void createGiftboxManager(Long userIdx, Long giftIdx) {

        GiftboxUser newGiftboxUser;
        try {
            newGiftboxUser = new GiftboxUser(
                em.find(Giftbox.class, giftIdx),
                em.find(User.class, userIdx),
                GiftboxUserRole.MANAGER,
                InvitationStatus.ACCEPTED);
            newGiftboxUser.createBaseEntity();
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR_NOT_FOUND);
        }

        try {
            // giftbox와 user를 연결
            em.persist(newGiftboxUser);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void addProductToGiftbox(Long giftboxIdx, Long productIdx) {
        GiftboxProduct deletedProduct = queryFactory.select(giftboxProduct)
                .from(giftboxProduct)
                .where(giftboxProduct.giftbox.idx.eq(giftboxIdx),
                        giftboxProduct.product.idx.eq(productIdx),
                        giftboxProduct.status.eq(Status.DELETED))
                .fetchOne();
        if (deletedProduct != null) {
            // status가 DELETED인 giftboxProduct가 존재할 경우 status를 ACTIVE로 변경
            queryFactory.update(giftboxProduct)
                    .set(giftboxProduct.status, Status.ACTIVE)
                    .set(giftboxProduct.updatedAt, LocalDateTime.now())
                    .where(giftboxProduct.giftbox.idx.eq(giftboxIdx),
                            giftboxProduct.product.idx.eq(productIdx),
                            giftboxProduct.status.eq(Status.DELETED))
                    .execute();
        } else {
            // giftbox와 product를 연결
            GiftboxProduct giftboxProduct = new GiftboxProduct(
                em.find(Giftbox.class, giftboxIdx),
                em.find(Product.class, productIdx),
                Status.ACTIVE
            );
            giftboxProduct.createBaseEntity();
            em.persist(giftboxProduct);
        }

    }

    public List<Giftbox> findAll() {
        // status가 ACTIVE인 모든 giftbox 조회
        List<Giftbox> giftboxList = queryFactory.select(giftbox)
                .from(giftbox)
                .where(giftbox.status.eq(Status.ACTIVE))
                .fetch();
        return giftboxList;
    }

    public List<Giftbox> findGiftboxOfUser(Long userIdx) {
        // userIdx로 status가 ACTIVE인 giftbox 조회
        return queryFactory.select(giftbox)
                .from(giftbox)
                .join(giftbox.participants, giftboxUser)
                .where(giftboxUser.user.idx.eq(userIdx),
                        giftboxUser.invitationStatus.eq(InvitationStatus.ACCEPTED),
                        giftbox.status.eq(Status.ACTIVE))
                .fetch();
    }


    public List<Product> findProductOfGiftbox(Long giftboxIdx) {
        // giftboxIdx로 status가 ACTIVE인 product 조회
        return queryFactory.select(product)
                .from(giftbox)
                .join(giftbox.products, giftboxProduct)
                .where(giftboxProduct.giftbox.idx.eq(giftboxIdx),
                        product.idx.eq(giftboxProduct.product.idx),
                        product.status.eq(ProductStatus.ACTIVE),
                        giftboxProduct.status.eq(Status.ACTIVE))
                .fetch();
    }


    @Transactional
    public void deleteProductOfGiftbox(Long giftboxIdx, Long productIdx) {
        queryFactory.update(giftboxProduct)
                .set(giftboxProduct.status, Status.DELETED)
                .set(giftboxProduct.deletedAt, LocalDateTime.now())
                .where(giftboxProduct.giftbox.idx.eq(giftboxIdx),
                        giftboxProduct.product.idx.eq(productIdx),
                        giftboxProduct.status.eq(Status.ACTIVE))
                .execute();
    }

    public boolean existsById(Long giftboxIdx) {
        // giftboxIdx로 status가 ACTIVE인 giftbox가 존재하는지 확인
        return queryFactory.selectOne()
                .from(giftbox)
                .where(giftbox.idx.eq(giftboxIdx),
                        giftbox.status.eq(Status.ACTIVE))
                .fetchFirst() != null;
    }

    public boolean existProductInGiftbox(Long giftboxIdx, Long productIdx) {
        // giftboxIdx와 productIdx로 status가 ACTIVE인 giftboxProduct가 존재하는지 확인
        return queryFactory.selectOne()
                .from(giftboxProduct)
                .where(giftboxProduct.giftbox.idx.eq(giftboxIdx),
                        giftboxProduct.product.idx.eq(productIdx),
                        giftboxProduct.status.eq(Status.ACTIVE))
                .fetchFirst() != null;
    }

    public boolean isManagerOfGiftbox(Long userIdx, Long giftboxIdx) {
        // userIdx와 giftboxIdx로 status가 ACTIVE인 giftboxUser가 존재하고 role이 MANAGER인지 확인
        return queryFactory.selectOne()
                .from(giftboxUser)
                .where(giftboxUser.user.idx.eq(userIdx),
                        giftboxUser.giftbox.idx.eq(giftboxIdx),
                        giftboxUser.invitationStatus.eq(InvitationStatus.ACCEPTED),
                        giftboxUser.userRole.eq(GiftboxUserRole.MANAGER))
                .fetchFirst() != null;
    }

    public boolean isParticipantOfGiftbox(Long userIdx, Long giftboxIdx) {
        // userIdx와 giftboxIdx로 status가 ACTIVE인 giftboxUser가 존재하는지 확인
        return queryFactory.selectOne()
                .from(giftboxUser)
                .where(giftboxUser.user.idx.eq(userIdx),
                        giftboxUser.giftbox.idx.eq(giftboxIdx),
                        giftboxUser.invitationStatus.eq(InvitationStatus.ACCEPTED))
                .fetchFirst() != null;
    }

    @Transactional
    public Long createPendingInvitation(Long giftboxIdx) {
        // giftboxIdx로 status가 ACTIVE인 giftboxUser를 생성하고 invitationStatus를 PENDING으로 설정
        GiftboxUser giftboxUser = new GiftboxUser(
            em.find(Giftbox.class, giftboxIdx),
            null,
            GiftboxUserRole.PARTICIPANT,
            InvitationStatus.PENDING
        );
        giftboxUser.createBaseEntity();
        em.persist(giftboxUser);
        return giftboxUser.getIdx();
    }

    public GiftboxUser findGiftboxByInvitationIdx(Long invitationIdx) {
        // invitationIdx로 status가 PENDING인 giftboxUser의 giftbox 조회
        return queryFactory.select(giftboxUser)
                .from(giftboxUser)
                .where(giftboxUser.idx.eq(invitationIdx))
                .fetchOne();
    }

    @Transactional
    public void acceptInvitationToGiftBox(Long userIdx, Long invitationIdx) {
        queryFactory.update(giftboxUser)
                .set(giftboxUser.invitationStatus, InvitationStatus.ACCEPTED)
                .set(giftboxUser.updatedAt, LocalDateTime.now())
                .set(giftboxUser.user, em.find(User.class, userIdx))
                .where(giftboxUser.idx.eq(invitationIdx),
                        giftboxUser.invitationStatus.eq(InvitationStatus.PENDING))
                .execute();
    }

    public List<GiftboxUser> findParticipantsOfGiftbox(Long giftboxIdx) {
        // giftboxIdx로 status가 ACTIVE인 giftboxUser 조회
        return queryFactory.select(giftboxUser)
                .from(giftboxUser)
                .where(giftboxUser.giftbox.idx.eq(giftboxIdx),
                        giftboxUser.invitationStatus.eq(InvitationStatus.ACCEPTED))
                .fetch();
    }

    public boolean existParticipantOfGiftbox(Long userIdx, Long idx) {
        // userIdx와 giftboxIdx로 status가 ACCEPTED인 giftboxUser가 존재하는지 확인
        return queryFactory.selectOne()
                .from(giftboxUser)
                .where(giftboxUser.user.idx.eq(userIdx),
                        giftboxUser.giftbox.idx.eq(idx),
                        giftboxUser.invitationStatus.eq(InvitationStatus.ACCEPTED))
                .fetchFirst() != null;
    }

    public boolean existsInvitationOfGiftbox(Long invitationIdx) {
        // invitationIdx로 status가 PENDING인 giftboxUser가 존재하는지 확인
        return queryFactory.selectOne()
                .from(giftboxUser)
                .where(giftboxUser.idx.eq(invitationIdx),
                        giftboxUser.invitationStatus.eq(InvitationStatus.PENDING))
                .fetchFirst() != null;
    }

    public List<GiftboxProduct> findGiftboxProductList(Long giftboxIdx) {
        // giftboxIdx로 status가 ACTIVE인 giftboxProduct 조회
        return queryFactory.select(giftboxProduct)
                .from(giftboxProduct)
                .where(giftboxProduct.giftbox.idx.eq(giftboxIdx),
                        giftboxProduct.status.eq(Status.ACTIVE))
                .fetch();
    }

}
