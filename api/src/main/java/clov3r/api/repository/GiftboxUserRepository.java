package clov3r.api.repository;

import clov3r.api.domain.entity.GiftboxUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftboxUserRepository extends JpaRepository<GiftboxUser, Long> {


}
