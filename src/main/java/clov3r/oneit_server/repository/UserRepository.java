package clov3r.oneit_server.repository;

import clov3r.oneit_server.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

  @Query("select count(u) > 0 from User u where u.idx = :userIdx and u.status = 'ACTIVE'")
  boolean existsByUserIdx(Long userIdx);

  boolean existsByEmail(String email);

  User findByEmail(String email);

  @Query("select u from User u where u.idx = :userIdx and u.status = 'ACTIVE'")
  User findByUserIdx(Long userIdx);

  @Query("select count(u) > 0 from User u where u.nickname = :nickname and u.status = 'ACTIVE'")
  boolean existsByNickname(String nickname);
}
