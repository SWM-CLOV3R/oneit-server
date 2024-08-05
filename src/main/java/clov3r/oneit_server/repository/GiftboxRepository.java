package clov3r.oneit_server.repository;

import clov3r.oneit_server.domain.request.PostGiftboxRequest;
import clov3r.oneit_server.domain.entity.Giftbox;
import clov3r.oneit_server.domain.entity.GiftboxUser;
import clov3r.oneit_server.domain.entity.User;
import clov3r.oneit_server.response.exception.BaseException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static clov3r.oneit_server.domain.entity.QGiftbox.giftbox;
import static clov3r.oneit_server.domain.entity.QUser.user;
import static clov3r.oneit_server.response.BaseResponseStatus.*;


@Repository
@RequiredArgsConstructor
public class GiftboxRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Transactional
    public Giftbox save(Giftbox input) {
        // giftbox를 생성하고 status를 ACTIVE로 설정, createdAt을 현재 시간으로 설정
        input.setStatus("ACTIVE");
        input.setCreatedAt(LocalDateTime.now());
        em.persist(input);
        em.flush();
        return input;

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
            Giftbox giftbox = em.find(Giftbox.class, idx);
            User user = em.find(User.class, createdUserIdx);
            newGiftboxUser = new GiftboxUser(giftbox, user, "MANAGER");
            newGiftboxUser.setCreatedAt(LocalDateTime.now());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR_NOT_FOUND);
        }
        // giftbox와 user를 연결
        em.persist(newGiftboxUser);
    }

}
