package clov3r.api.giftbox.repository.GiftboxProduct;


import clov3r.api.product.domain.collection.GiftboxProductVoteId;
import clov3r.api.giftbox.domain.entity.GiftboxProductVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiftboxProductVoteRepository extends
    JpaRepository<GiftboxProductVote, GiftboxProductVoteId>,
    GiftboxProductCustomVoteRepository {

}
