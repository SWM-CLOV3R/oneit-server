package clov3r.domain.domains.entity;

import clov3r.domain.domains.status.PurchaseStatus;
import clov3r.domain.domains.status.Status;
import clov3r.domain.domains.type.EmojiName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "giftbox_product")
public class GiftboxProduct extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giftbox_idx")
    private Giftbox giftbox;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_idx")
    private Product product;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private PurchaseStatus purchaseStatus;

    @Column(name = "like_count")
    private int likeCount;

    @Column(name = "dislike_count")
    private int dislikeCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "emoji_name")
    private EmojiName emojiName;

    public GiftboxProduct(Giftbox giftbox, Product product, Status status) {
        this.giftbox = giftbox;
        this.product = product;
        this.status = status;
        this.createBaseEntity();
    }
}
