package clov3r.oneit_server.domain.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    private String createdAt;
    private String updatedAt;
    private String deletedAt;
    private String status;

}
