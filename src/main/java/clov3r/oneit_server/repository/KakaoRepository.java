package clov3r.oneit_server.repository;

import clov3r.oneit_server.domain.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class KakaoRepository {

    private final EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public boolean existsUser(String email) {
        return em.createQuery("select count(u) > 0 from User u where u.email = :email", Boolean.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    public User findUser(String email) {
        return em.createQuery("select u from User u where u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    @Transactional
    public void saveRefreshToken(String refreshToken, String email) {
        em.createQuery("update User u set u.refreshToken = :refreshToken where u.email = :email")
                .setParameter("refreshToken", refreshToken)
                .setParameter("email", email)
                .executeUpdate();
    }
}
