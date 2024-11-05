package clov3r.domain.domains.entity;

import clov3r.domain.domains.entity.id.GiftboxProductVoteId;
import clov3r.domain.domains.status.VoteStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "giftbox_product_vote")
public class GiftboxProductVote {

  @EmbeddedId
  private GiftboxProductVoteId id;

  @Enumerated(EnumType.STRING)
  @Column(name = "vote")
  private VoteStatus vote;

}
