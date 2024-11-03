package clov3r.api.giftbox.repository.GiftboxUser;

import clov3r.api.giftbox.domain.entity.GiftboxUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftboxUserRepository extends JpaRepository<GiftboxUser, Long> {

  @Query("select gu.sender.idx from GiftboxUser gu where gu.idx = :invitationIdx")
  Long findSenderByInvitationIdx(Long invitationIdx);

  @Query("select gu from GiftboxUser gu where gu.user.idx = :userIdx")
  List<GiftboxUser> findByUser(Long userIdx);

}
