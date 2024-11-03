package clov3r.api.common.domain.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@MappedSuperclass
public class BaseEntity {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @PrePersist
    public void createBaseEntity() {
        this.createdAt = LocalDateTime.now();

    }

    public void updateBaseEntity() {
        if (this.createdAt != null) {
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void deleteBaseEntity() {
        this.deletedAt = LocalDateTime.now();
    }

    public void restoreBaseEntity() {
        this.deletedAt = null;
    }
}
