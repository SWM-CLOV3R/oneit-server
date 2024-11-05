package clov3r.domain.domains.entity;

import clov3r.domain.domains.status.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "collection")
public class Collection extends BaseEntity {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idx;

  private String name;
  private String description;

  @Column(name = "thumbnail_url")
  private String thumbnailUrl;

  @Enumerated(EnumType.STRING)
  private Status status;

}
