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

    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private ZonedDateTime deletedAt;

    public void createBaseEntity() {
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    public void updateBaseEntity() {
        if (this.createdAt != null) {
            this.updatedAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        }
    }

    public void deleteBaseEntity() {
        this.deletedAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    }

}
