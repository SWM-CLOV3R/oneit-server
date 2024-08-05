package clov3r.oneit_server.repository;

import static clov3r.oneit_server.domain.entity.QUser.user;

import clov3r.oneit_server.domain.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public void save(User user) {
        em.persist(user);
    }
    public boolean existsUser(Long userIdx) {
        return em.createQuery("select count(u) > 0 from User u where u.idx = :userIdx", Boolean.class)
                .setParameter("userIdx", userIdx)
                .getSingleResult();
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
