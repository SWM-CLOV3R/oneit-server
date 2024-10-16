package clov3r.api.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuthRepository {

    private final EntityManager em;
    @Transactional
    public void saveRefreshToken(String refreshToken, String email) {
        em.createQuery("update User u set u.refreshToken = :refreshToken where u.email = :email")
                .setParameter("refreshToken", refreshToken)
                .setParameter("email", email)
                .executeUpdate();
    }
}
