package clov3r.api.giftbox.domain.entity;

import clov3r.api.giftbox.domain.status.AccessStatus;
import clov3r.api.common.domain.status.Status;
import clov3r.api.common.domain.entity.BaseEntity;
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
import lombok.Getter;

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

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "giftbox", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GiftboxUser> participants;
    @OneToMany(mappedBy = "giftbox", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GiftboxProduct> products;

    public Giftbox(String name, String description, LocalDate deadline, Long createdUserIdx, AccessStatus accessStatus, Status status) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.createdUserIdx = createdUserIdx;
        this.accessStatus = accessStatus;
        this.status = status;
        this.createBaseEntity();
    }

    public Giftbox() {

    }

    public Giftbox(String name, LocalDate deadline, Long userIdx, Status status) {
        this.name = name;
        this.deadline = deadline;
        this.createdUserIdx = userIdx;
        this.status = status;
        this.createBaseEntity();
    }
}
