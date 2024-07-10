package clov3r.oneit_server.domain;

import jakarta.persistence.*;
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
    private Long id;

    private String keyword;

    @Column(name = "keyword_description")
    private String description;

}
