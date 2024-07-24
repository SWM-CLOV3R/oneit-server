package clov3r.oneit_server.domain.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    private String createdAt;
    private String updatedAt;
    private String deletedAt;
    private String status;

    public BaseEntity() {
        // 현재 시간을 createdAt, updatedAt에 저장
        this.createdAt = new Date().toString();
        this.updatedAt = new Date().toString();
        this.deletedAt = null;
        this.status = "ACTIVE";
    }

}
