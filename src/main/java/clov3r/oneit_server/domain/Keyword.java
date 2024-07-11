package clov3r.oneit_server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "keyword")
public class Keyword {

    @Id
    @GeneratedValue
    @Column(name = "keyword_idx")
    private Long idx;

    @JsonIgnore
    @Column(name = "product_idx")
    private Long productIdx;

    private String keyword;

    @Column(name = "keyword_description")
    private String description;

    // createKeyword


}
