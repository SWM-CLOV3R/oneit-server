package clov3r.oneit_server.repository;

import clov3r.oneit_server.domain.collection.GiftboxProductVoteId;
import clov3r.oneit_server.domain.entity.GiftboxProductVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiftboxProductRepository extends
    JpaRepository<GiftboxProductVote, GiftboxProductVoteId>,
    GiftboxProductCustomRepository {

}
