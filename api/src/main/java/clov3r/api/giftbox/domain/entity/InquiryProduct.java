package clov3r.api.giftbox.domain.entity;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import clov3r.api.giftbox.domain.data.EmojiName;
import clov3r.api.common.domain.entity.BaseEntity;
import clov3r.api.product.domain.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "inquiry_product")
public class InquiryProduct extends BaseEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long idx;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "inquiry_idx")
  private Inquiry inquiry;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "product_idx")
  private Product product;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "giftbox_idx")
  private Giftbox giftbox;

  @Column(name = "emoji_idx")
  private Long emojiIdx;

  @Enumerated(EnumType.STRING)
  @Column(name = "emoji_name")
  private EmojiName emojiName;

  public InquiryProduct(Inquiry inquiry, Product product, Giftbox giftbox) {
    this.inquiry = inquiry;
    this.product = product;
    this.giftbox = giftbox;
    createBaseEntity();
  }
}
