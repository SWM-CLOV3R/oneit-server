package clov3r.api.giftbox.domain.entity;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import clov3r.api.giftbox.domain.data.EmojiName;
import clov3r.api.giftbox.domain.status.PurchaseStatus;
import clov3r.api.common.domain.status.Status;
import clov3r.api.common.domain.entity.BaseEntity;
import clov3r.api.product.domain.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "giftbox_product")
public class GiftboxProduct extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long idx;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "giftbox_idx")
    private Giftbox giftbox;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_idx")
    private Product product;

    @Enumerated(STRING)
    private Status status;

    @Enumerated(STRING)
    private PurchaseStatus purchaseStatus;

    @Column(name = "like_count")
    private int likeCount;

    @Column(name = "dislike_count")
    private int dislikeCount;

    @Enumerated(STRING)
    @Column(name = "emoji_name")
    private EmojiName emojiName;

    public GiftboxProduct(Giftbox giftbox, Product product, Status status) {
        this.giftbox = giftbox;
        this.product = product;
        this.status = status;
        this.createBaseEntity();
    }
}
