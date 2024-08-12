package clov3r.oneit_server.repository;

import static clov3r.oneit_server.domain.entity.QUser.user;

import clov3r.oneit_server.domain.data.status.Status;
import clov3r.oneit_server.domain.data.status.UserStatus;
import clov3r.oneit_server.domain.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public void save(User user) {
        em.persist(user);
    }
    public boolean existsUser(Long userIdx) {
        // status가 ACTIVE인 user가 존재하는지 확인
        return queryFactory.selectOne()
                .from(user)
                .where(user.idx.eq(userIdx)
                        .and(user.status.eq(UserStatus.ACTIVE)))
                .fetchFirst() != null;

    }

    public User findUser(Long userIdx) {
        return em.find(User.class, userIdx);
    }


    public boolean existsUserByEmail(String email) {
        return em.createQuery("select count(u) > 0 from User u where u.email = :email", Boolean.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    public User findUserByEmail(String email) {
        return em.createQuery("select u from User u where u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
    }
}
