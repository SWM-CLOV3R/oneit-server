package clov3r.oneit_server.domain.entity;

import clov3r.oneit_server.domain.data.AccessStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Table(name = "giftbox")
public class Giftbox extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String name;
    private String description;
    private LocalDate deadline;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_user_idx")
    private Long createdUserIdx;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_status")
    private AccessStatus accessStatus;

    @OneToMany(mappedBy = "giftbox", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GiftboxUser> participants;
    @OneToMany(mappedBy = "giftbox", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GiftboxProduct> products;

    public Giftbox(String name, String description, LocalDate deadline, Long createdUserIdx, AccessStatus accessStatus) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.createdUserIdx = createdUserIdx;
        this.accessStatus = accessStatus;

        this.createBaseEntity();
    }

    public Giftbox() {

    }
}
