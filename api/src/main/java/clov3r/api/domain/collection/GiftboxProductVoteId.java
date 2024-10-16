package clov3r.api.domain.collection;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Data
public class GiftboxProductVoteId implements Serializable {
  @Column(name = "giftbox_idx")
  private Long giftboxIdx;

  @Column(name = "product_idx")
  private Long productIdx;

  @Column(name = "user_idx")
  private Long userIdx;

  @Column(name = "browser_uuid")
  private String browserUuid;
}
