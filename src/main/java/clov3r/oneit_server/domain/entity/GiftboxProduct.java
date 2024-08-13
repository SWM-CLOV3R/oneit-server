package clov3r.oneit_server.domain.entity;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

import clov3r.oneit_server.domain.data.status.InvitationStatus;
import clov3r.oneit_server.domain.data.status.Status;
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
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
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

    @Enumerated(EnumType.STRING)
    private Status status;


    public GiftboxProduct(Giftbox giftbox, Product product, Status status) {
        this.giftbox = giftbox;
        this.product = product;
        this.status = status;

        this.createBaseEntity();
    }

    public GiftboxProduct() {

    }
}
