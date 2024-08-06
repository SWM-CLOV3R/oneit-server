package clov3r.oneit_server.domain.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private String status;

    public BaseEntity() {
    }

    public void createBaseEntity() {
        this.createdAt = LocalDateTime.now();
        this.status = "ACTIVE";
    }

    public void updateBaseEntity() {
        this.updatedAt = LocalDateTime.now();
        this.status = "ACTIVE";
    }

    public void deleteBaseEntity() {
        this.deletedAt = LocalDateTime.now();
        this.status = "DELETED";
    }

}
