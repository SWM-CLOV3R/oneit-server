package clov3r.oneit_server.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

public class GiftboxProduct {

    @Id @GeneratedValue
    private Long idx;


    private Giftbox giftbox;
    private Product product;
}
