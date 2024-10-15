package clov3r.api.repository;


import clov3r.api.domain.collection.GiftboxProductVoteId;
import clov3r.api.domain.entity.GiftboxProductVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiftboxProductVoteRepository extends
    JpaRepository<GiftboxProductVote, GiftboxProductVoteId>,
    GiftboxProductCustomVoteRepository {

}
