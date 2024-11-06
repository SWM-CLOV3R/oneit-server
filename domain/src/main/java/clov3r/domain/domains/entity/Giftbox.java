package clov3r.domain.domains.entity;

import clov3r.domain.domains.status.AccessStatus;
import clov3r.domain.domains.status.Status;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
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

    @Enumerated(EnumType.STRING)
    private Status status;

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
        this.status = Status.ACTIVE;
        this.createBaseEntity();
    }

    public Giftbox(String name, LocalDate deadline, Long userIdx) {
        this.name = name;
        this.deadline = deadline;
        this.createdUserIdx = userIdx;
        this.status = Status.ACTIVE;
        this.createBaseEntity();
    }
}
