package clov3r.oneit_server.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
public class Giftbox extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String name;
    private String description;
    private Date deadline;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_user_idx")
    private Long createdUserIdx;
    @Column(name = "access_status")
    private String accessStatus;

    @OneToMany(mappedBy = "idx")
    private List<User> participants;
    @OneToMany(mappedBy = "idx")
    private List<Product> products;

    public Giftbox(String name, String description, Date deadline, Long createdUserIdx, String accessStatus) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.createdUserIdx = createdUserIdx;
        this.accessStatus = accessStatus;
    }

    public Giftbox() {

    }
}
