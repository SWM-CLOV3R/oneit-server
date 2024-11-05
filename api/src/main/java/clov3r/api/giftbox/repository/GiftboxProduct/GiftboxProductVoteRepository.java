package clov3r.api.giftbox.repository.GiftboxProduct;


import clov3r.domain.domains.entity.GiftboxProductVote;
import clov3r.domain.domains.entity.id.GiftboxProductVoteId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiftboxProductVoteRepository extends
    JpaRepository<GiftboxProductVote, GiftboxProductVoteId>,
    GiftboxProductCustomVoteRepository {

}
