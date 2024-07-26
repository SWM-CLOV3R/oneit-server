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
    @Transactional
    public void saveRefreshToken(String refreshToken, String email) {
        em.createQuery("update User u set u.refreshToken = :refreshToken where u.email = :email")
                .setParameter("refreshToken", refreshToken)
                .setParameter("email", email)
                .executeUpdate();
    }
}
