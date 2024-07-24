package clov3r.oneit_server.domain.entity;

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
    @Column(name = "idx")
    private Long idx;

    @Column(name = "name")
    private String name;

    @Column(name = "field")
    private String field;

    @Column(name = "keyword_description")
    private String description;

}
