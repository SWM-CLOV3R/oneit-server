package clov3r.oneit_server.repository;

import clov3r.oneit_server.domain.entity.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class KakaoRepository {

    private final EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public boolean existsUser(String kakaoAccessToken) {
        return em.createQuery("select count(u) > 0 from User u where u.kakaoAccessToken = :kakaoAccessToken", Boolean.class)
                .setParameter("kakaoAccessToken", kakaoAccessToken)
                .getSingleResult();
    }

    public User findUser(String kakaoAccessToken) {
        return em.createQuery("select u from User u where u.kakaoAccessToken = :kakaoAccessToken", User.class)
                .setParameter("kakaoAccessToken", kakaoAccessToken)
                .getSingleResult();
    }
}
