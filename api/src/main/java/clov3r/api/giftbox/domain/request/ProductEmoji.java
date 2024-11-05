package clov3r.api.giftbox.domain.request;
import clov3r.domain.domains.type.EmojiName;
import lombok.Getter;

@Getter
public class ProductEmoji {
  private Long productIdx;
  private EmojiName emojiName;

}
