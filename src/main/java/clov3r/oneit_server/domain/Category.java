package clov3r.oneit_server.domain;

import jakarta.persistence.*;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "category", indexes = {
        @Index(name = "idx_category_idx", columnList = "idx"),
        @Index(name = "idx_parent_idx", columnList = "parent_idx")
})
public class Category {

    @Id @GeneratedValue
    @Column(name = "idx")
    private Long idx;

    @Column(name = "name")
    private String name;

    @Column(name = "level")
    private Integer level;

    @Column(name = "display_name")
    private String displayName;

    @ManyToOne
    @JoinColumn(name = "parent_idx", insertable=false, updatable=false, nullable = true)
    private Category parent;
//
//    @ManyToOne
//    @JoinColumn(name = "parent_category_idx")
//    private Category parentCategory;
}
