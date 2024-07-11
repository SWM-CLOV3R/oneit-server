package clov3r.oneit_server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class ProductKeyword {

    @Id
    @GeneratedValue
    @Column(name = "product_keyword_idx")
    private Long idx;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_idx")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "keyword_idx")
    private Keyword keyword;

    private Integer intensity;

}