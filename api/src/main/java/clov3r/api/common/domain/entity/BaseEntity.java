package clov3r.api.common.domain.entity;

import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

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

}
