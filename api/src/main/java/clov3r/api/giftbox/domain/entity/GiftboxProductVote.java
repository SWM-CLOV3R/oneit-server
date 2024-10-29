package clov3r.api.giftbox.domain.entity;

import clov3r.api.common.domain.collection.GiftboxProductVoteId;
import clov3r.api.giftbox.domain.status.VoteStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "giftbox_product_vote")
public class GiftboxProductVote {

  @EmbeddedId
  private GiftboxProductVoteId id;

  @Enumerated(EnumType.STRING)
  @Column(name = "vote")
  private VoteStatus vote;

}
