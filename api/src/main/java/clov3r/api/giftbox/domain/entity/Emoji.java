package clov3r.api.giftbox.domain.entity;

import static jakarta.persistence.EnumType.STRING;

import clov3r.api.giftbox.domain.data.EmojiName;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class Emoji {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idx;

  @Enumerated(STRING)
  private EmojiName name;

  private String content;

}
