package clov3r.oneit_server.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_idx")
    private Category parent;

    @Column(name = "parent_category_name")
    private String parentCategoryName;
//
//    @ManyToOne
//    @JoinColumn(name = "parent_category_idx")
//    private Category parentCategory;
}
