package clov3r.api.domain.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class GiftboxInquiryResult extends BaseEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long idx;

  @ManyToOne
  @JoinColumn(name = "giftbox_idx")
  private Giftbox giftbox;

  @ManyToOne
  @JoinColumn(name = "product_idx")
  private Product product;

  @ManyToOne
  @JoinColumn(name = "emoji_idx")
  private Emoji emoji;

  public GiftboxInquiryResult(Giftbox giftbox, Product product) {
    this.giftbox = giftbox;
    this.product = product;
    createBaseEntity();
  }

  public GiftboxInquiryResult() {

  }
}
