package clov3r.oneit_server.domain.entity;

import jakarta.persistence.Column;
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
