package clov3r.api.repository;

import clov3r.api.domain.entity.GiftboxUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftboxUserRepository extends JpaRepository<GiftboxUser, Long> {

  @Query("select gu.sender.idx from GiftboxUser gu where gu.idx = :invitationIdx")
  Long findSenderByInvitationIdx(Long invitationIdx);
}
