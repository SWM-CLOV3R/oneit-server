package clov3r.oneit_server.repository;

import clov3r.oneit_server.domain.DTO.GiftboxDTO;
import clov3r.oneit_server.domain.entity.Giftbox;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static clov3r.oneit_server.domain.entity.QGiftbox.giftbox;


@Repository
@RequiredArgsConstructor
public class GiftboxRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Transactional
    public Giftbox save(Giftbox giftbox) {
        // giftbox를 생성
        em.persist(giftbox);
        return giftbox;
    }

    public void updateImageUrl(Long idx, String imageUrl) {
        // giftbox의 imageUrl을 수정
        // with QueryDSL
        queryFactory.update(giftbox)
                .set(giftbox.imageUrl, imageUrl)
                .where(giftbox.idx.eq(idx))
                .execute();

    }

    public GiftboxDTO findById(Long giftboxIdx) {
        // giftbox를 idx로 조회
        Giftbox giftbox = em.find(Giftbox.class, giftboxIdx);
        return new GiftboxDTO(giftboxIdx, giftbox.getName(), giftbox.getDescription(), giftbox.getDeadline(), giftbox.getImageUrl(), giftbox.getCreatedUserIdx(), giftbox.getAccessStatus());
    }
}
