package clov3r.oneit_server.domain.entity;

import static jakarta.persistence.GenerationType.*;

import clov3r.oneit_server.domain.data.status.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Collection extends BaseEntity {

  @Id @GeneratedValue(strategy = IDENTITY)
  private Long idx;

  private String name;
  private String description;

  @Column(name = "thumbnail_url")
  private String thumbnailUrl;

  @Enumerated(EnumType.STRING)
  private Status status;

}
