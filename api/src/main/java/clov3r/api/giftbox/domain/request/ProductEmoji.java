package clov3r.api.giftbox.domain.request;
import clov3r.api.giftbox.domain.data.EmojiName;
import lombok.Getter;

@Getter
public class ProductEmoji {
  private Long productIdx;
  private EmojiName emojiName;

}
