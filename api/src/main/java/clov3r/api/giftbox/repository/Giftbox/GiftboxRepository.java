package clov3r.api.giftbox.repository.Giftbox;

import clov3r.domain.domains.entity.Giftbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftboxRepository extends JpaRepository<Giftbox, Long>, GiftboxCustomRepository {

}
