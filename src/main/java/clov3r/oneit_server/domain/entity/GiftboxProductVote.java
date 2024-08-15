package clov3r.oneit_server.domain.entity;

import static jakarta.persistence.FetchType.*;

import clov3r.oneit_server.domain.collection.GiftboxProductVoteId;
import clov3r.oneit_server.domain.data.status.VoteStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
