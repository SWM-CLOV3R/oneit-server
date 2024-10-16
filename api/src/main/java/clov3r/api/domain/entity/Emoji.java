package clov3r.api.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class Emoji {

  @Id
  @GeneratedValue
  private Long idx;
  private String name;
  private String content;

}
