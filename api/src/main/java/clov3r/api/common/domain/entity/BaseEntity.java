package clov3r.api.common.domain.entity;

import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    private LocalDateTime createdAt;
    private ZonedDateTime updatedAt;
    private LocalDateTime deletedAt;

    public void createBaseEntity() {
        this.createdAt = LocalDateTime.now();
        log.info("createdAt: {}", this.createdAt);

    }

    public void updateBaseEntity() {
        if (this.createdAt != null) {
            this.updatedAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
            log.info("updatedAt: {}", this.updatedAt);
        }
    }

    public void deleteBaseEntity() {
        this.deletedAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        log.info("deletedAt: {}", this.deletedAt);
    }

}
