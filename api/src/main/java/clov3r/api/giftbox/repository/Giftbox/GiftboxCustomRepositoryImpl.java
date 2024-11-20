package clov3r.api.giftbox.repository.Giftbox;

import static clov3r.domain.domains.entity.QGiftbox.giftbox;
import static clov3r.domain.domains.entity.QGiftboxProduct.giftboxProduct;
import static clov3r.domain.domains.entity.QGiftboxUser.giftboxUser;
import static clov3r.domain.domains.entity.QInquiry.inquiry;
import static clov3r.domain.domains.entity.QProduct.product;

import clov3r.api.giftbox.domain.request.PostGiftboxRequest;
import clov3r.api.auth.repository.UserRepository;
import clov3r.domain.domains.entity.Giftbox;
import clov3r.domain.domains.entity.GiftboxProduct;
import clov3r.domain.domains.entity.GiftboxUser;
import clov3r.domain.domains.entity.Product;
import clov3r.domain.domains.status.InvitationStatus;
import clov3r.domain.domains.status.ProductStatus;
import clov3r.domain.domains.status.Status;
import clov3r.domain.domains.status.UserStatus;
import clov3r.domain.domains.type.GiftboxUserRole;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@RequiredArgsConstructor
public class GiftboxCustomRepositoryImpl implements GiftboxCustomRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    private final UserRepository userRepository;

    public Giftbox saveGiftbox(Giftbox giftbox) {
        // giftbox를 생성하고 status를 ACTIVE로 설정, createdAt을 현재 시간으로 설정
        em.persist(giftbox);
        em.flush();
        return giftbox;

    }

    public void updateImageUrl(Long idx, String imageUrl) {
        // giftbox의 imageUrl을 수정
        queryFactory.update(giftbox)
                .set(giftbox.imageUrl, imageUrl)
                .where(giftbox.idx.eq(idx))
                .execute();

    }

    public Giftbox findByIdx(Long giftboxIdx) {
        // idx로 status가 ACTIVE인 row만 조회
        Giftbox result = queryFactory.select(giftbox)
                .from(giftbox)
                .where(giftbox.idx.eq(giftboxIdx)
                    .and(giftbox.status.eq(Status.ACTIVE)))
                .fetchOne();
        return result;
    }

    public void deleteByIdx(Long giftboxIdx) {
        // status가 ACTIVE인 row일 경우에만 DELETED로 변경, deletedAt을 현재 시간으로 변경
        queryFactory.update(giftbox)
                .set(giftbox.status, Status.DELETED)
                .set(giftbox.deletedAt, LocalDateTime.now())
                .where(giftbox.idx.eq(giftboxIdx),
                        giftbox.status.eq(Status.ACTIVE))
                .execute();
    }

    public void updateGiftbox(Long giftboxIdx, PostGiftboxRequest request) {
        // giftbox의 정보를 수정
        queryFactory.update(giftbox)
                .set(giftbox.name, request.getName())
                .set(giftbox.deadline, request.getDeadline())
                .set(giftbox.updatedAt, LocalDateTime.now())
                .where(giftbox.idx.eq(giftboxIdx),
                        giftbox.status.eq(Status.ACTIVE))
                .execute();
    }

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

    public boolean existsByIdx(Long giftboxIdx) {
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

    public GiftboxUser findGiftboxByInvitationIdx(Long invitationIdx) {
        // invitationIdx로 status가 PENDING인 giftboxUser의 giftbox 조회
        return queryFactory.select(giftboxUser)
                .from(giftboxUser)
                .where(giftboxUser.idx.eq(invitationIdx))
                .fetchOne();
    }

    public void acceptInvitationToGiftBox(Long userIdx, Long invitationIdx) {
        queryFactory.update(giftboxUser)
                .set(giftboxUser.invitationStatus, InvitationStatus.ACCEPTED)
                .set(giftboxUser.updatedAt, LocalDateTime.now())
                .set(giftboxUser.user, userRepository.findByUserIdx(userIdx))
                .where(giftboxUser.idx.eq(invitationIdx),
                        giftboxUser.invitationStatus.eq(InvitationStatus.PENDING))
                .execute();
    }

    public List<GiftboxUser> findParticipantsOfGiftbox(Long giftboxIdx) {
        // giftboxIdx로 status가 ACTIVE인 giftboxUser 조회
        // userIdx가 ACTIVE인 giftboxUser만 조회
        return queryFactory.select(giftboxUser)
                .from(giftboxUser)
                .where(giftboxUser.giftbox.idx.eq(giftboxIdx)
                        .and(giftboxUser.invitationStatus.eq(InvitationStatus.ACCEPTED))
                        .and(giftboxUser.user.status.eq(UserStatus.ACTIVE)))
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
        // invitationIdx로 giftboxUser가 존재하는지 확인
        return queryFactory.selectOne()
                .from(giftboxUser)
                .where(giftboxUser.idx.eq(invitationIdx))
                .fetchFirst() != null;
    }

    public List<GiftboxProduct> findGiftboxProductList(Long giftboxIdx) {
        // giftboxIdx로 status가 ACTIVE인 giftboxProduct 조회
        return queryFactory.select(giftboxProduct)
                .from(giftboxProduct)
                .where(giftboxProduct.giftbox.idx.eq(giftboxIdx),
                        giftboxProduct.status.eq(Status.ACTIVE))
                .where(giftboxProduct.product.status.eq(ProductStatus.ACTIVE)
                    .or(giftboxProduct.product.status.eq(ProductStatus.INVALID)))
                .fetch();
    }


    public Giftbox findByInquiryIdx(Long inquiryIdx) {
        return queryFactory.select(giftbox)
                .from(inquiry)
                .where(inquiry.idx.eq(inquiryIdx))
                .fetchOne();
    }

    public List<GiftboxProduct> searchProductInGiftbox(String searchKeyword, Long giftboxIdx) {
        // giftboxIdx로 status가 ACTIVE인 product 중 검색어가 포함된 product 조회
        return queryFactory.select(giftboxProduct)
                .from(giftboxProduct)
                .where(giftboxProduct.giftbox.idx.eq(giftboxIdx),
                    giftboxProduct.status.eq(Status.ACTIVE))
                .where(giftboxProduct.product.name.contains(searchKeyword)
                        .or(giftboxProduct.product.brandName.contains(searchKeyword))
                        .or(giftboxProduct.product.displayTags.contains(searchKeyword))
                        .or(giftboxProduct.product.description.contains(searchKeyword)))
                .where(giftboxProduct.product.status.eq(ProductStatus.ACTIVE)
                    .or(giftboxProduct.product.status.eq(ProductStatus.INVALID)))
                .fetch();
    }

}
